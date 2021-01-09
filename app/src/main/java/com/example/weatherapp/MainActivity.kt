package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*
import kotlinx.android.synthetic.main.lin_coord.*
import kotlinx.android.synthetic.main.main_card.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), ClickFromOtherActivity {
    private var list: MutableList<ModelDay> = mutableListOf()
    private var openCoord = false
    private var lat = "55.4507"
    private var lon = "37.3656"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menu.setOnClickListener {
            if (!openCoord)
                motion.transitionToEnd()
            else
                motion.transitionToStart()

            openCoord =! openCoord
        }

        inc_create.findViewById<LinearLayout>(R.id.lin_button).setOnClickListener {
            if (textLat.text.toString() != lat && textLon.text.toString() != lon) {
                lat = if (textLat.text.isNotEmpty())
                    textLat.text.toString()
                else
                    "55.4507"

                lon = if (textLon.text.isNotEmpty())
                    textLon.text.toString()
                else
                    "37.3656"

                initDataFromApi()
            }
            motion.transitionToStart()
            openCoord = false
        }
        initDataFromApi()
        }

    private fun initDateForMainCard(model: ModelDay, pos:Int){
        var te = model.temp!!.day!!.toInt().toString() + "°"
        if ("-" !in te)
            te = "+$te"
        t.text = te
        textWeather.text = model.weather!![0].description!!.capitalize()
        val dateT = Date((list[pos].dt!!.toLong() * 1000))
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
        initDateForMainCard(model, position)
    }

    private fun initDataFromApi(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(OpenWeatherMapInterface::class.java)
            .getDaileData(lat, lon).enqueue(object : Callback<ModelWeather> {
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
                    initDateForMainCard(response.body()!!.daily!![0], 0)
                    rec.adapter!!.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ModelWeather>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }
}