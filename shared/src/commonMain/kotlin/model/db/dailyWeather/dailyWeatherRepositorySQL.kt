package model.db.actualWeather

import kotlinx.coroutines.flow.flow
import model.domain.hourlyWeather
import model.db.dailyWeather.DailyDataSource
import model.db.dailyWeather.dailyWeatherRepository
import model.db.dailyWeather.toDailyWeather
import model.db.dailyWeather.toHourlyWeatherList
import model.domain.hourlyWeatherList

class dailyWeatherRepositorySQL(
    private val dataSource: DailyDataSource
): dailyWeatherRepository {

    override fun getAll() = flow<Result<List<hourlyWeather>>>{
        dataSource.getAll()
            .collect{i -> emit(Result.success(i.map { j -> j.toDailyWeather() }))}
    }

    fun getAlliOS()=flow<hourlyWeatherList>{
        getAll().collect{i -> i.onSuccess { j -> emit(j.toHourlyWeatherList()) }}
    }

    override suspend fun insert(date: String, latitude: Double, longitude: Double, temperature: Double, code: Long) {
        dataSource.insert(
            date = date,
            longitude = longitude,
            latitude = latitude,
            temperature = temperature,
            code = code)
    }

    override suspend fun deleteAll() {
        dataSource.deleteAll()
    }
}