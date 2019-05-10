package za.co.dubedivine.taximath.ui.main

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import kotlinx.android.synthetic.main.main_fragment.*
import za.co.dubedivine.taximath.R
import za.co.dubedivine.taximath.adapter.TaxiRowSeatsAdapter
import za.co.dubedivine.taximath.model.TaxiRowSeats
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.content.res.ResourcesCompat
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast
import za.co.dubedivine.taximath.util.setBackgroundTint


class MainFragment : Fragment() {

    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

    private lateinit var mDetector: GestureDetectorCompat
    private lateinit var scaleDownAnim: Animation
    private lateinit var scaleUpAnim: Animation
    private lateinit var drawableTaxiOne: Drawable
    private lateinit var drawableTaxiTwo: Drawable
    // by default we calculating for taxi one // used in inner class below
    private var isOnSwipeCalculatingForTaxiOne: Boolean = true

    //    private lateinit var viewModel: MainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val taxiRowSeatsAdapter = TaxiRowSeatsAdapter(ArrayList(), ArrayList(), context!!)
        scaleDownAnim = AnimationUtils.loadAnimation(activity, R.anim.scale_down_animation)
        scaleUpAnim = AnimationUtils.loadAnimation(activity, R.anim.scale_up_animation)
        mDetector = GestureDetectorCompat(activity, MyGestureListener())

        drawableTaxiOne = ResourcesCompat.getDrawable(resources, R.drawable.text_view_rounded_corner, null)!!
        drawableTaxiTwo = ResourcesCompat.getDrawable(resources, R.drawable.text_view_rounded_corner_2, null)!!

        ledgerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = taxiRowSeatsAdapter
        }

        fabCalculate.setOnClickListener {
            calculate()
        }

        //Setup next action here
        setViewNextIMEListenerForEditText(et_taxi_price_person, et_amount)
        setViewNextIMEListenerForEditText(et_taxi_price_person_two, et_amount)
        setViewNextIMEListenerForEditText(et_amount, et_number_of_people)

        // when the user presses the calculate this will happen
        et_number_of_people.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calculate()
            }
            true
        }

        // called when the TextInputEditText gains or looses focus
        val onChangeFocusListener: (View, Boolean) -> Unit = { view: View, hasFocus: Boolean ->
            // TODO: remove error from view man
            val taxiPricePerson = (view as TextInputEditText).text.toString()
                val taxiPricePersonNumber = if (taxiPricePerson.isNotBlank()) taxiPricePerson.toDouble() else 0.0
                if (!hasFocus) {
                    Log.d(TAG, "The taxi price lost focus bro")
                    val taxiRowSeatsArrayList = ArrayList<TaxiRowSeats>().apply {
                        add(TaxiRowSeats(2, taxiPricePersonNumber * 2))
                        add(TaxiRowSeats(3, taxiPricePersonNumber * 3))
                        add(TaxiRowSeats(4, taxiPricePersonNumber * 4))
                        add(TaxiRowSeats(15, taxiPricePersonNumber * 15))
                    }
                    when (view.id) {
                        R.id.et_taxi_price_person -> taxiRowSeatsAdapter.addAll(taxiRowSeatsArrayList, 0)
                        R.id.et_taxi_price_person_two -> taxiRowSeatsAdapter.addAll(taxiRowSeatsArrayList, 1)
                    }
                }

            Log.d(TAG, "Has a bit of focus bro $hasFocus")
        }
        et_taxi_price_person.setOnFocusChangeListener(onChangeFocusListener)
        et_taxi_price_person_two.setOnFocusChangeListener(onChangeFocusListener)

        val onSwipeListener = { view: View, event: MotionEvent ->
            mDetector.onTouchEvent(event)
        }

        et_amount.setOnTouchListener(onSwipeListener)
        tv_layout_amount.setOnTouchListener(onSwipeListener)
        card_view.setOnTouchListener(onSwipeListener)
        et_number_of_people.setOnTouchListener(onSwipeListener)
        tv_layout_number_of_people.setOnTouchListener(onSwipeListener)
        tv_taxi_mode.setOnTouchListener(onSwipeListener)
        // TODO wrong and causes a crash when the user swipes on trackpad left and right 🙄, smh
//        ledgerRecyclerView.setOnTouchListener(onSwipeListener)

        //TODO: consistent naming
        fabCalculate.setOnTouchListener(onSwipeListener)

        tv_share.setOnClickListener {
            val intent = Intent(android.content.Intent.ACTION_SEND)
            intent.type = "text/plain"
            val shareBodyText = "Please check out this awesome app!, It helps you to easily calculate change," +
                    " when sitting in the front seat of a taxi and it looks nice 😉.\n\n " +
                    "Click the link below to find out more and download app from the Google play: \n" +
                    "https://play.google.com/store/apps/details?id=za.co.dubedivine.taximath"
//            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title")
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText)
            startActivity(Intent.createChooser(intent, "Choose sharing method"))
        }

        val madeBy = getString(R.string.made_by)
        val spannableString = SpannableString(madeBy)
        spannableString.setSpan(UnderlineSpan(), madeBy.indexOf('@') + 1, madeBy.length - 4, 0)
        tv_made_by.text = spannableString

        tv_made_by.setOnClickListener {
            try {
                this.activity?.packageManager?.getPackageInfo("com.twitter.android", 0)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=423458669"))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(activity, "Sorry could not open @divinedube`s profile because you " +
                        "do not have the Twitter app on your phone.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun calculate() {
        val amountGivenString = et_amount.text.toString()
        val numberOfPeopleString = et_number_of_people.text.toString()
        val pricePersonInTaxiString = et_taxi_price_person.text.toString()
        val pricePersonInTaxiStringTwo = et_taxi_price_person_two.text.toString()

        // todo use kotlin lazy
        // naming convention is bad
        clearErrorViewFromEditText(arrayOf(tv_layout_taxi_price_per_person,
                tv_layout_number_of_people,
                tv_layout_amount, et_taxi_price_2))

        when {
            isOnSwipeCalculatingForTaxiOne -> {
                if (pricePersonInTaxiString.isBlank()) {
                    tv_layout_taxi_price_per_person.error = "REQUIRED: How much does the taxi cost per seat 1."
                    et_taxi_price_person.requestFocus()
                    return
                }
            } else -> {
                if (pricePersonInTaxiStringTwo.isBlank()) {
                    et_taxi_price_2.error = "REQUIRED: How much does the taxi cost per seat 2."
                    et_taxi_price_person_two.requestFocus()
                    return
                }
            }
        }

        if (amountGivenString.isBlank()) {
            tv_layout_amount.error = "REQUIRED: How much money did the passengers give you?"
            et_amount.requestFocus()
            return
        }
        if (numberOfPeopleString.isBlank()) {
//            Toast.makeText(context, "REQUIRED: For how many people is the amount for", Toast.LENGTH_SHORT).show()
            tv_layout_number_of_people.error = "REQUIRED: For how many people is the amount for"
            et_number_of_people.requestFocus()
            return
        }

        //TODO make better
        val amountGiven = amountGivenString.toDouble()
        val numberOfPeople = et_number_of_people.text.toString().toInt()

        // taxiFair
        val taxiPrice = if (isOnSwipeCalculatingForTaxiOne) {
            et_taxi_price_person.text.toString().toDouble()
        } else
            et_taxi_price_person_two.text.toString().toDouble()

        val priceGivenNumberOfPeople = taxiPrice * numberOfPeople
        val change = amountGiven - priceGivenNumberOfPeople

        tv_status_display.text = getString(R.string.tv_status_display, amountGiven, numberOfPeople, taxiPrice)

        if (change < 0) {
            tv_display.setTextColor(ContextCompat.getColor(context!!, android.R.color.holo_red_light))
            tv_display.text = getString(R.string.display_text_short, change)
        } else {
            tv_display.setTextColor(ContextCompat.getColor(context!!, android.R.color.black))
            tv_display.text = getString(R.string.display_text, change)
        }

        Log.d(TAG, "the change : $change is given $amountGiven | $numberOfPeople | $taxiPrice")
    }


    private fun clearErrorViewFromEditText(view: Array<TextInputLayout>) {
        view.forEach {
            it.error = null
            it.isErrorEnabled = false
        }
    }

    private fun setViewNextIMEListenerForEditText(fromEditText: EditText, toEditText: EditText) {
        fromEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                toEditText.requestFocus()
            }
            true
        }
    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_MIN_DISTANCE = 120
        private val SWIPE_MAX_OFF_PATH = 250
        private val SWIPE_THRESHOLD_VELOCITY = 200

        override fun onDown(event: MotionEvent): Boolean {
            Log.d(TAG, "onDown")
            return false
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            Log.d(TAG, "onFling: ")
            try {
                if (Math.abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH)
                    return false
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                  //  Toast.makeText(activity, "Left Swipe", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onFling left: ")
                    manipulateViews(false)


                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                  //  Toast.makeText(activity, "Right Swipe", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onFling Right: ")
                    manipulateViews(true)
                }
            } catch (e: Exception) {
                // nothing
            }

            return false
        }

        private fun manipulateViews(swipeDirection: Boolean) {
//            // early return when the user does not do the right things
//            if (swipeDirection == isOnSwipeCalculatingForTaxiOne) {
//                Toast.makeText(context, "Swipe the other direction to calculate")
//                return
//            }

            fabCalculate.startAnimation(scaleDownAnim) // global variable are bad ey
            tv_taxi_mode.startAnimation(scaleDownAnim)
            when {
                swipeDirection -> {
                    fabCalculate.setBackgroundTint(R.color.colorTaxiTwo)
                    tv_taxi_mode.setBackgroundDrawable(drawableTaxiTwo)
                    tv_taxi_mode.text = getString(R.string.taxi_mode, 2)
                    isOnSwipeCalculatingForTaxiOne = false
                }
                else -> {
                    fabCalculate.setBackgroundTint(R.color.colorTaxiOne)
                    tv_taxi_mode.setBackgroundDrawable(drawableTaxiOne)
                    tv_taxi_mode.text = getString(R.string.taxi_mode, 1)
                    isOnSwipeCalculatingForTaxiOne = true
                }
            }
            fabCalculate.startAnimation(scaleUpAnim)
            tv_taxi_mode.startAnimation(scaleUpAnim)
        }
    }

}
