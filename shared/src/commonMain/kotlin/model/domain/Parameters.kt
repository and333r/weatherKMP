package model.domain

import kotlinx.serialization.Serializable


@Serializable
data class Parameters (
    val time: List<String>,
    val temperature_2m: List<Double>,
    val relative_humidity_2m: List<Int>,
    val apparent_temperature: List<Double>,
    val precipitation_probability: List<Int>,
    val weather_code: List<Int>
)