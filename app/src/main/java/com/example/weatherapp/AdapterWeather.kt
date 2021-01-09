package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class AdapterWeather(private val list: MutableList<ModelDay>, var nowPos: Int): RecyclerView.Adapter<AdapterWeather.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textT = itemView.findViewById<TextView>(R.id.textT)
        val textDate = itemView.findViewById<TextView>(R.id.textDate)
        val img = itemView.findViewById<ImageView>(R.id.imgSmall)
        val back = itemView.findViewById<LinearLayout>(R.id.lin_item)
        val textDayWeek = itemView.findViewById<TextView>(R.id.textDayWeek)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == nowPos){
            holder.back.background = holder.itemView.context.resources.getDrawable(R.drawable.shape_gr)
        }else{
            holder.back.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.backItemView))
        }
        Picasso.get()
                .load("http://openweathermap.org/img/wn/${list[position].weather?.get(0)!!.icon!!}@4x.png")
                .into(holder.img)
        var t = list[position].temp!!.day!!.toInt().toString()

        if ("-" !in t)
            t = "+$t"
        holder.textT.text = t

        val dateT = Date((list[position].dt!!.toLong() * 1000))
        val cal = Calendar.getInstance()
        cal.time = dateT
        holder.textDayWeek.text = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG_FORMAT, Locale.ENGLISH)

        holder.textDate.text = SimpleDateFormat("dd.MM.yyyy").format((Date((list[position].dt!!.toLong() * 1000))))

        holder.itemView.setOnClickListener {
            val activity = holder.itemView.context as MainActivity
            activity.clickToItemWeek(position)
            nowPos = position
            holder.back.background = holder.itemView.context.resources.getDrawable(R.drawable.shape_gr)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = list.size
}