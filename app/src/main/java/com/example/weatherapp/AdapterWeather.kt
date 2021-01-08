package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class AdapterWeather(private val list: MutableList<ModelDay>): RecyclerView.Adapter<AdapterWeather.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textT = itemView.findViewById<TextView>(R.id.textT)
        val textDate = itemView.findViewById<TextView>(R.id.textDate)
        val img = itemView.findViewById<ImageView>(R.id.imgSmall)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
                .load("http://openweathermap.org/img/wn/10d@4x.png")
                .into(holder.img)
        var t = list[position].temp!!.day!!.toInt().toString()

        if ("-" !in t)
            t = "+$t"
        holder.textT.text = t

        holder.textDate.text = SimpleDateFormat("dd.MM.yyyy").format((Date((list[position].dt!!.toLong() * 1000))))
    }

    override fun getItemCount(): Int = list.size
}