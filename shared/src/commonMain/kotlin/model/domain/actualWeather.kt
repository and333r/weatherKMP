package model.domain

data class actualWeather (
    val latitude: Double,
    val longitude: Double,
    val temperature:Double,
    val humidity:Int,
    val code: Int,
    val relativeT:Double,
    val precipitation: Int
)