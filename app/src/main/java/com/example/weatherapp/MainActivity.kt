package com.example.weatherapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
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
    private var openWindow = false
    private var startScreen = true
    private var lat = "55.4507"
    private var lon = "37.3656"
    private var cityAQI: ModelAQI? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showStartScreen()
        textCity.setOnClickListener {
            if (!openWindow)
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

        textLon.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_GO){
                initNewCity()
                true
            }else{
                false
            }
        }

        initDataFromApiWeather()
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

            initDataFromApiWeather()
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
        initWeatherIcon(model.weather!![0].icon.toString(), img)
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
        if (lat != modelCity.lat!! && lon != modelCity.lon!!) {
            lat = modelCity.lat!!
            lon = modelCity.lon!!
            initDataFromApiWeather()
        }else
            hideCoord()
    }

    private fun initWeatherIcon(icon: String, img: ImageView){
        Picasso.get()
                .load("http://openweathermap.org/img/wn/${icon}@4x.png")
                .into(img)
    }

    private fun initDataFromApiWeather(){
        showStartScreen()
        initRetrofit("https://api.openweathermap.org/data/2.5/").create(OpenWeatherMapInterface::class.java)
            .getDaileData(lat, lon).enqueue(object : Callback<ModelWeather> {
                override fun onResponse(
                    call: Call<ModelWeather>,
                    response: Response<ModelWeather>
                ) {
                    hideCoord()
                    list = response.body()!!.daily!!.toMutableList()
                    rec.apply {
                        layoutManager =
                            LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
                            adapter = AdapterWeather(list, 0)
                    }
                    textCity.text = response.body()!!.timezone!!.substringAfter('/')
                    initDateForMainCard(response.body()!!.daily!![0], 0)
                    rec.adapter!!.notifyDataSetChanged()
                    initAQI()
                }

                override fun onFailure(call: Call<ModelWeather>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    private fun initAQI(){
        initRetrofit("https://api.waqi.info/feed/geo:$lat;$lon/").create(OpenWeatherMapInterface::class.java).getAQI().enqueue(object: Callback<ModelAQI>{
            override fun onResponse(call: Call<ModelAQI>, response: Response<ModelAQI>) {
                cityAQI = response.body()!!
                textAQI.text = cityAQI!!.data!!.aqi!!.toString()
                hideStartScreen()
            }

            override fun onFailure(call: Call<ModelAQI>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG)
                        .show()
            }
        })
    }

    private fun initRetrofit(baseUrl: String): Retrofit{
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
    }

    private fun hideCoord(){
        motion.requestFocus()
        hideKeyboard()
        motion.setTransition(R.id.tra_show)
        motion.transitionToStart()
        openWindow = false
    }

    private fun showCoord(){
        motion.setTransition(R.id.tra_show)
        motion.transitionToEnd()
        openWindow = true
    }

    private fun showStartScreen(){
        startScreen = true
        motion.setTransition(R.id.tra_hide)
        motion.transitionToEnd()
    }

    private fun hideStartScreen(){
        startScreen = false
        motion.setTransition(R.id.tra_hide)
        motion.transitionToStart()
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}