package za.co.dubedivine.taximath.adapter

import android.content.Context
import android.support.annotation.IntRange
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import za.co.dubedivine.taximath.R
import za.co.dubedivine.taximath.model.TaxiRowSeats
import kotlin.collections.ArrayList


/*
* add animation please
* */

// TODO: should come back with a Pair of the two rowseats
// TODO current method not the best method to build a system
// Could use the update method will see
class TaxiRowSeatsAdapter(private val itemsTaxiOne: ArrayList<TaxiRowSeats>,
                          private val itemsTaxiTwo: ArrayList<TaxiRowSeats>,
                          context: Context) : RecyclerView.Adapter<TaxiRowSeatsAdapter.RowSeatsViewHolder>() {

    private val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RowSeatsViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_ledger, viewGroup, false)
        return RowSeatsViewHolder(view)
    }

    // the size is constant so thats a bonus bro
    override fun getItemCount(): Int = itemsTaxiOne.size


    override fun onBindViewHolder(holder: RowSeatsViewHolder, position: Int) {
        if (itemsTaxiOne.isNotEmpty()) {
            holder.bind(itemsTaxiOne[position])
        }
        if (itemsTaxiTwo.isNotEmpty()) {
            holder.bindTaxiTwo(itemsTaxiTwo[position])
        }

    }

    inner class RowSeatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //  val tv_transaction = view.findViewById<TextView>(R.id.tv_transaction)
        private val tvNumPeopleInSeat = view.findViewById<TextView>(R.id.tv_num_people_in_seat)!!
        private val tvSeatsPriceOne = view.findViewById<TextView>(R.id.tv_seats_price_1)!!
        private val tvSeatsPriceTwo = view.findViewById<TextView>(R.id.tv_seats_price_2)!!
        private val tvPeople= view.findViewById<TextView>(R.id.tv_people)!!

        private fun bindCommon(numberOfPeople: Int) {
            tvNumPeopleInSeat.text = numberOfPeople.toString()

            if(numberOfPeople == 15) {
                tvNumPeopleInSeat.setTextColor(ResourcesCompat.getColor(itemView.context.resources, android.R.color.holo_red_dark, null))
                tvPeople.setTextColor(ResourcesCompat.getColor(itemView.context.resources, android.R.color.holo_red_dark, null))
            }
            itemView.startAnimation(animation)
            itemView.setOnClickListener {} // click listener

        }

        // TODO refactor these lines of code please they are very similar

        fun bind(rowSeatTaxiOne: TaxiRowSeats) {
            val (numberOfPeople, priceForPeople) = rowSeatTaxiOne
            // val displayText = context.getString(R.string.tv_item_ledger_status_display, amount, numberOfPeople, priceGivenNumberOfPeople, change)
            tvSeatsPriceOne.text = if(priceForPeople == 0.0) "R --" else "R $priceForPeople"
            bindCommon(numberOfPeople)
        }

        fun bindTaxiTwo(taxiRowSeats: TaxiRowSeats) {
            val (numberOfPeople, priceForPeople) = taxiRowSeats
            tvSeatsPriceTwo.text = if(priceForPeople == 0.0) "R --" else "R $priceForPeople"
            bindCommon(numberOfPeople)
        }
    }

    // could make the taxiIndex to boolean but chose to make it int for readability and brevity
    fun addAll(taxiRowSeats: ArrayList<TaxiRowSeats>, @IntRange(from = 0L, to = 1L) taxiIndex: Int) {
        when(taxiIndex) {
            0 -> {
                itemsTaxiOne.clear()
                itemsTaxiOne.addAll(taxiRowSeats)
            }
            1 -> {
                itemsTaxiTwo.clear()
                itemsTaxiTwo.addAll(taxiRowSeats)
            }
        }
        notifyDataSetChanged()
    }

    //TODO should be function that changes the heading of the item view to reflect the taxi mode
}

