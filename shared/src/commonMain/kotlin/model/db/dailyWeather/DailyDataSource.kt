package model.db.dailyWeather

import com.db.DailyWeather
import kotlinx.coroutines.flow.Flow

interface DailyDataSource {
    suspend fun getAll(): Flow<List<DailyWeather>>
    suspend fun insert(date: String, latitude: Double, longitude: Double, temperature: Double, code: Long)
    suspend fun deleteAll()
}