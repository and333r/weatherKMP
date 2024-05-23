package model.domain

data class daySpecWeather (
    val date: String,
    val latitude: Double,
    val longitude: Double,
    val temperatureMax: Double,
    val temperatureMin: Double,
    val code: Int
)