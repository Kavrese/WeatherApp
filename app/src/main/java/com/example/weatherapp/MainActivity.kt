package com.example.weatherapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*
import kotlinx.android.synthetic.main.lin_coord.*
import kotlinx.android.synthetic.main.lin_history.*
import kotlinx.android.synthetic.main.main_card.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), ClickFromOtherOBJ {
    private var list: MutableList<ModelDay> = mutableListOf()
    private var listCity: MutableList<ModelCity> = mutableListOf()
    private var openCoord = false
    private var openHistory = false
    private var lat = "55.4507"
    private var lon = "37.3656"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menu.setOnClickListener {
            if (!openCoord)
                showCoord()
            else
                hideCoord()
        }

        rec_history.apply {
            adapter = AdapterHistory(listCity)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        inc_create.findViewById<LinearLayout>(R.id.lin_button).setOnClickListener {
            initNewCity()
        }

        history.setOnClickListener {
            if (!openHistory)
                showHistory()
            else
                hideHistory()

        }

        textLon.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_GO){
                initNewCity()
                true
            }else{
                false
            }
        }

        initDataFromApi()
        }

    private fun initNewCity(){
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
        hideCoord()
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

        listCity.add(ModelCity(textCity.text.toString(), lat, lon))
        rec_history.adapter!!.notifyDataSetChanged()
    }

    override fun clickToItemWeek(position: Int) {
        val model = list[position]
        initDateForMainCard(model, position)
    }

    override fun clickToItemHistory(position: Int) {
        val modelCity = listCity[position]
        lat = modelCity.lat!!
        lon = modelCity.lon!!
        initDataFromApi()
        hideCoord()
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

    private fun hideCoord(){
        hideKeyboard()
        motion.setTransition(R.id.tra_show)
        motion.transitionToStart()
        openCoord = false
    }

    private fun showCoord(){
        motion.setTransition(R.id.tra_show)
        motion.transitionToEnd()
        openCoord = true
    }

    private fun hideHistory(){
        motion.setTransition(R.id.tra_show_history)
        motion.transitionToStart()
        openHistory = false
    }

    private fun showHistory(){
        motion.setTransition(R.id.tra_show_history)
        motion.transitionToEnd()
        openHistory = true
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}