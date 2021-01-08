package com.example.weatherapp

class ModelWeather (
    var timezone: String? = null,
    var daily: List<ModelDay>? = null
)

class ModelDay (
    var dt: Int? = null,
    var wind_speed: Float? = null,
    var temp: Temps? = null,
    var feels_like: FeelsLikeTemps? = null,
    var weather: Weather? = null
)

class Weather(
    var main: String? = null,
    var descriprtion: String? = null,
    var clouds: Int? = null
)

class Temps(
    var day: Float? = null,
    var min: Float? = null,
    var max: Float? = null,
    var night: Float? = null,
    var eve: Float? = null,
    var morn: Float? = null
)

class FeelsLikeTemps(
    var day: Float? = null,
    var night: Float? = null,
    var eve: Float? = null,
    var morn: Float? = null
)