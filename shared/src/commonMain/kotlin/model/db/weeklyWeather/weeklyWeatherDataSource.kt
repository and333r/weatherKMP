package model.db.weeklyWeather

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.db.WeatherAppDatabaseKMP
import com.db.WeeklyWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class weeklyWeatherDataSource(db: WeatherAppDatabaseKMP): WeekDataSource {


    private val queries = db.weeklyWeatherQueries
    //    Set id = null to let SQLDelight autogenerate the id
    override suspend fun insert(date: String, latitude: Double, longitude: Double, temperatureMax: Double, temperatureMin: Double, code: Long) {
        queries.insert(
            id = null,
            date = date,
            longitude = longitude,
            latitude = latitude,
            temperatureMax = temperatureMax,
            temperatureMin = temperatureMin,
            code = code
        )
    }

    override suspend fun getAll(): Flow<List<WeeklyWeather>> {
        return queries.getAll().asFlow().mapToList(Dispatchers.IO)
    }

    override suspend fun deleteAll() {
        queries.deleteAll()
    }
}