package model.db.actualWeather

import kotlinx.coroutines.flow.Flow
import model.domain.actualWeather

interface actualWeatherRepository {
    fun getAll():  Flow<Result<List<actualWeather>>>
    suspend fun insert(hour: Long, latitude: Double, longitude: Double, temperature: Double, humidity: Long, code: Long, relativeT: Double, precipitation: Long)

    suspend fun deleteAll()
}