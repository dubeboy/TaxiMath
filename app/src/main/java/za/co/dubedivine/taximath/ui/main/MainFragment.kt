package za.co.dubedivine.taximath.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.main_fragment.*
import za.co.dubedivine.taximath.R
import za.co.dubedivine.taximath.adapter.LedgerAdapter
import za.co.dubedivine.taximath.model.Money

class MainFragment : Fragment() {

    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

//    private lateinit var viewModel: MainViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // get the recycler view first
        // we are using kotlin extensions here so that we dont have to do findviewbyId

        //set layoutManager
        val ledgerAdapter = LedgerAdapter(ArrayList(), context!!)

        ledgerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = ledgerAdapter
        }

        fabCalculate.setOnClickListener {
            calculate(ledgerAdapter)
        }


        setViewNextIMEListenerForEditText(et_taxi_price_person, et_amount)
        setViewNextIMEListenerForEditText(et_amount, et_number_of_people)

        // when the user presses the the calculate this will happen
        et_number_of_people.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calculate(ledgerAdapter)
            }
            true
        }

    }

    private fun calculate(ledgerAdapter: LedgerAdapter) {
        val amountString = et_amount.text.toString()
        val numberOfPeopleString = et_number_of_people.text.toString()
        val pricePersonInTaxiString = et_taxi_price_person.text.toString()

        // todo use kotlin lazy
        clearErrorViewFromEditText(arrayOf(tv_layout_taxi_price_per_person, tv_layout_number_of_people, tv_layout_amount))


        if (pricePersonInTaxiString.isBlank()) {
            Toast.makeText(context, "Enter the price of the taxi per person", Toast.LENGTH_SHORT).show()
            tv_layout_taxi_price_per_person.error = "Enter the price of the taxi per person"
            et_taxi_price_person.requestFocus()
            return
        }

        if (amountString.isBlank()) {
            Toast.makeText(context, "Enter the amount that was given", Toast.LENGTH_SHORT).show()
            tv_layout_amount.error = "Enter amount given"
            et_amount.requestFocus()
            return
        }
        if (numberOfPeopleString.isBlank()) {
            Toast.makeText(context, "Enter the number of people", Toast.LENGTH_SHORT).show()
            tv_layout_number_of_people.error = "Number of people"
            et_number_of_people.requestFocus()
            return
        }




        val amount = amountString.toDouble()
        val numberOfPeople = et_number_of_people.text.toString().toInt()
        val pricePersonInTaxi = et_taxi_price_person.text.toString().toDouble()

        val priceGivenNumberOfPeople = pricePersonInTaxi * numberOfPeople
        val change = amount - priceGivenNumberOfPeople
        //            <!--change for given R10 for 2 people | price-->

        tv_status_display.text = getString(R.string.tv_status_display, amount, numberOfPeople, priceGivenNumberOfPeople)
        if (change < 0) {
            tv_display.setTextColor(ContextCompat.getColor(context!!, android.R.color.holo_red_light))
            tv_display.text = getString(R.string.display_text_short, change)
        } else {
            tv_display.text = getString(R.string.display_text, change)
        }

        Log.d(TAG, "the change : $change is given $amount | $numberOfPeople | $pricePersonInTaxi")

        ledgerAdapter.add(Money(numberOfPeople, amount, change, priceGivenNumberOfPeople))
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

}
