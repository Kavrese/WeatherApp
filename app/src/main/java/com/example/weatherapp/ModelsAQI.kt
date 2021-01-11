package com.example.weatherapp

class ModelAQI (
        var status: String? = null,
        var data: Data? = null
)
class Data(
        var aqi: Int? = null,
        var forecast: Forecast? = null
)

class Forecast (
    var daily: Daily? = null
)

class Daily (
        var o3: List<DailyData>? = null,
        var pm10: List<DailyData>? = null,
        var pm25: List<DailyData>? = null,
        var uvi: List<DailyData>? = null
)

class DailyData (
        var avg: Int? = null,
        var day: String? = null,
        var max: Int? = null,
        var min: Int? = null
)
