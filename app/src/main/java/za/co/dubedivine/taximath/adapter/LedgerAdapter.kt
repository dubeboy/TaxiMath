package za.co.dubedivine.taximath.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import za.co.dubedivine.taximath.R
import za.co.dubedivine.taximath.model.Money
import java.util.*


/*
* add animation please
* so when you click on each item it should expand and show transaction history
* */
class LedgerAdapter(private val items: ArrayList<Money>, private val context: Context) : RecyclerView.Adapter<LedgerAdapter.LViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_ledger, viewGroup, false)
        return LViewHolder(view)
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: LViewHolder, position: Int) {
        holder.bind(items[position])
    }

   inner class LViewHolder(view: View) : RecyclerView.ViewHolder(view) {

     //  val tv_transaction = view.findViewById<TextView>(R.id.tv_transaction)
       val tvNumPeopleInSeat = view.findViewById<TextView>(R.id.tv_num_people_in_seat)
       val tvSeatsPriceOne = view.findViewById<TextView>(R.id.tv_seats_price_1)
       val tvSeatsPriceTwo = view.findViewById<TextView>(R.id.tv_seats_price_2)


        fun bind(money: Money) {
            val (numberOfPeople, amount, change, priceGivenNumberOfPeople) = money
            val displayText = context.getString(R.string.tv_item_ledger_status_display, amount, numberOfPeople, priceGivenNumberOfPeople, change)
          //  tv_transaction.text = displayText


            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.startAnimation(animation)
            itemView.setOnClickListener {

            }
        }
    }

    fun add(money: Money) {
        items.add(money)
        if (items.size == 1) {
            notifyItemInserted(0)
        } else {
            Collections.swap(items, items.lastIndex, 0);
            notifyItemMoved(items.lastIndex, 0)
        }
    }

}