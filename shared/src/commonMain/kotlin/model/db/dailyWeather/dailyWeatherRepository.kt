package model.db.dailyWeather

import kotlinx.coroutines.flow.Flow
import model.domain.hourlyWeather

interface dailyWeatherRepository {
    fun getAll():  Flow<Result<List<hourlyWeather>>>
    suspend fun insert(date: String, latitude: Double, longitude: Double, temperature: Double, code: Long)
    suspend fun deleteAll()
}