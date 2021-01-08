package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterWeather(private val list: MutableList<ModelDay>): RecyclerView.Adapter<AdapterWeather.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textT = itemView.findViewById<TextView>(R.id.textT)
        val textDate = itemView.findViewById<TextView>(R.id.textDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var t = list[position].temp!!.day!!.toInt().toString()

        if ("-" !in t)
            t = "+$t"
        holder.textT.text = t

        holder.textDate.text = list[position].dt.toString()
    }

    override fun getItemCount(): Int = list.size
}