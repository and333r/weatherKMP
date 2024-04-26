package model.domain

import kotlinx.serialization.Serializable

@Serializable
data class weekWeather (
    val latitude: Double,
    val longitude: Double,
    val hourly:Parameters
    )