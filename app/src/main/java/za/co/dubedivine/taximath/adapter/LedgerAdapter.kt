package za.co.dubedivine.taximath.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import za.co.dubedivine.taximath.model.Money


/*
* add animation please
* */
class LedgerAdapter(private val items: ArrayList<Money>) : RecyclerView.Adapter<LedgerAdapter.LViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(0, viewGroup, false)
        return LViewHolder(view)
    }

    override fun getItemCount(): Int =    items.size


    override fun onBindViewHolder(holder: LViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class LViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(money: Money) {

        }
    }
}