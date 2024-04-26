package model.domain

data class dayWeather (
    val latitude: Double,
    val longitude: Double,
    val temperatures:List<Double>,
    val humidities:List<Int>,
    val codes: List<Int>,
    val relativeTs:List<Double>,
    val precipitations: List<Int>

)