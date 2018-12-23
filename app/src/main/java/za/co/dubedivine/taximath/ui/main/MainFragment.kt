package za.co.dubedivine.taximath.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.main_fragment.*
import za.co.dubedivine.taximath.R
import za.co.dubedivine.taximath.adapter.LedgerAdapter

class MainFragment : Fragment() {

    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // get the recycler view first
        // we are using kotlin extensions here so that we dont have to do findviewbyId

        //set layoutManager
        ledgerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = LedgerAdapter(ArrayList())
        }

        fabCalculate.setOnClickListener {

            val amountString = et_amount.text.toString()
            val numberOfPeopleString = number_of_people.text.toString()
            val pricePersonInTaxiString = et_taxi_price_person.text.toString()

            tv_layout_taxi_price_per_person.error = null
            tv_layout_amount.error = null
            tv_layout_number_of_people.error = null


            if (pricePersonInTaxiString.isBlank()) {
                Toast.makeText(context, "Enter price per person", Toast.LENGTH_SHORT).show()
                tv_layout_taxi_price_per_person.error = "Enter price per person"
                return@setOnClickListener
            }

            if (amountString.isBlank()) {

                Toast.makeText(context, "Enter the amount", Toast.LENGTH_SHORT).show()
                tv_layout_amount.error = "Enter the amount"
                return@setOnClickListener

            }
            if (numberOfPeopleString.isBlank()) {
                Toast.makeText(context, "Enter the number of people", Toast.LENGTH_SHORT).show()
                tv_layout_number_of_people.error = "Number of people"
                return@setOnClickListener
            }


            val amount = amountString.toDouble()
            val numberOfPeople = number_of_people.text.toString().toInt()
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


        }


    }

}
