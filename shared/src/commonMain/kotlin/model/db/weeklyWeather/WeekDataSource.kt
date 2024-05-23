package model.db.weeklyWeather

import com.db.DailyWeather
import com.db.WeeklyWeather
import kotlinx.coroutines.flow.Flow

interface WeekDataSource {
    suspend fun getAll(): Flow<List<WeeklyWeather>>
    suspend fun insert(date: String, latitude: Double, longitude: Double, temperatureMax: Double, temperatureMin: Double, code: Long)
    suspend fun deleteAll()
}