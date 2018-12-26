package za.co.dubedivine.taximath.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import za.co.dubedivine.taximath.R
import za.co.dubedivine.taximath.model.Money
import java.util.*


/*
* add animation please
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

   inner class LViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

       val tv_transaction = view.findViewById<TextView>(R.id.tv_transaction)

        fun bind(money: Money) {
            val (numberOfPeople, amount, change, priceGivenNumberOfPeople) = money
            val displayText = context.getString(R.string.tv_item_ledger_status_display, amount, numberOfPeople, priceGivenNumberOfPeople, change)
            tv_transaction.text = displayText

            itemView.setOnClickListener {

            }
        }
    }

    fun add(money: Money) {
        items.add(money)
        Collections.swap(items, items.lastIndex, 0);
        notifyDataSetChanged()
        notifyItemMoved(items.lastIndex, 0)
    }

}