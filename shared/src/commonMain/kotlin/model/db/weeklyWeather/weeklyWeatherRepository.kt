package model.db.weeklyWeather

import kotlinx.coroutines.flow.Flow
import model.domain.daySpecWeather

interface weeklyWeatherRepository {
    fun getAll():  Flow<Result<List<daySpecWeather>>>
    suspend fun insert(date: String, latitude: Double, longitude: Double, temperatureMax: Double, temperatureMin: Double, code: Long)
    suspend fun deleteAll()
}