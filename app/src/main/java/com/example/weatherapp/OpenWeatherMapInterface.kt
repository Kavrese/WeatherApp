package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenWeatherMapInterface {
    @GET("onecall?exclude=hourly,minutely,current&appid=25259f9d664c1c63ad35756dcf1765fd&units=metric&lang=en")
    fun getDaileData(@Query("lat") lat:String, @Query("lon") lon:String): Call<ModelWeather>

    @GET("geo:{lat};{lon}/?token=597e27403198d6e82e06d4375088d4d7336ccd6c")
    fun getAQI(@Path("lat") lat: String, @Path("lon") lon: String): Call<ModelAQI>
}