package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*
import kotlinx.android.synthetic.main.main_card.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), ClickFromRecycler {
    private var list: MutableList<ModelDay> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            retrofit.create(OpenWeatherMapInterface::class.java)
                .getDaileData("55.4507", "37.3656").enqueue(object : Callback<ModelWeather> {
                override fun onResponse(
                    call: Call<ModelWeather>,
                    response: Response<ModelWeather>
                ) {
                    list = response.body()!!.daily!!.toMutableList()
                    rec.apply {
                        layoutManager =
                            LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
                        adapter = AdapterWeather(list, 0)
                    }
                    textCity.text = response.body()!!.timezone!!.substringAfter('/')
                    initDateForMainCard(response.body()!!.daily!![0])
                }

                override fun onFailure(call: Call<ModelWeather>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
        }

    private fun initDateForMainCard(model: ModelDay){
        var te = model.temp!!.day!!.toInt().toString() + "°"
        if ("-" !in te)
            te = "+$te"
        t.text = te
        textWeather.text = model.weather!![0].description!!.capitalize()
        val dateT = Date((list[0].dt!!.toLong() * 1000))
        val cal = Calendar.getInstance()
        cal.time = dateT
        date.text = SimpleDateFormat("dd.MM.yyyy").format((dateT)) + ", ${cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG_FORMAT, Locale.ENGLISH)}"
        Picasso.get()
            .load("http://openweathermap.org/img/wn/${model.weather!![0].icon}@4x.png")
            .into(img)
        Clouds.text = model.clouds!!.toString() + '%'
        Humidity.text = model.humidity!!.toString() + '%'
        Wind.text = model.wind_speed.toString() + "\nm/s"
    }

    override fun clickToItem(position: Int) {
        val model = list[position]
        initDateForMainCard(model)
    }
}