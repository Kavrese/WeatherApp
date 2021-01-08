package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var list: MutableList<ModelDay> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            retrofit.create(OpenWeatherMapInterface::class.java)
                .getDaileData("33.441792", "-94.037689").enqueue(object : Callback<ModelWeather> {
                override fun onResponse(
                    call: Call<ModelWeather>,
                    response: Response<ModelWeather>
                ) {
                    list = response.body()!!.daily!!.toMutableList()
                    rec.apply {
                        layoutManager =
                            LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
                        adapter = AdapterWeather(list)
                    }


                }

                override fun onFailure(call: Call<ModelWeather>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG)
                        .show()
                }

            })
        }
}