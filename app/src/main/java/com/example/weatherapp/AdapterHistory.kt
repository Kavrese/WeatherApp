package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterHistory(private var list: MutableList<ModelCity>): RecyclerView.Adapter<AdapterHistory.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cityName = itemView.findViewById<TextView>(R.id.cityNameHistory)
        val lat = itemView.findViewById<TextView>(R.id.textLatHistory)
        val lon = itemView.findViewById<TextView>(R.id.textLonHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityName.text = list[position].nameCity!!
        holder.lat.text = list[position].lat!!
        holder.lon.text = list[position].lon!!

        holder.itemView.setOnClickListener {
            val activity = holder.itemView.context as MainActivity
            activity.clickToItemHistory(position)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}