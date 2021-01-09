package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenWeatherMapInterface {
    @GET("onecall?exclude=hourly,minutely,current&appid=25259f9d664c1c63ad35756dcf1765fd&units=metric&lang=ru")
    fun getDaileData(@Query("lat") lat:String, @Query("lon") lon:String): Call<ModelWeather>
}