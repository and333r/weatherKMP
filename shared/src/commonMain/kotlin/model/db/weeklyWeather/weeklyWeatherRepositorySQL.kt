package model.db.actualWeather

import kotlinx.coroutines.flow.flow
import model.domain.daySpecWeather
import model.domain.hourlyWeather
import model.db.dailyWeather.DailyDataSource
import model.db.dailyWeather.dailyWeatherRepository
import model.db.dailyWeather.toDailyWeather
import model.db.weeklyWeather.WeekDataSource
import model.db.weeklyWeather.toWeeklyWeather
import model.db.weeklyWeather.weeklyWeatherRepository

class weeklyWeatherRepositorySQL(
    private val dataSource: WeekDataSource
): weeklyWeatherRepository {

    override fun getAll() = flow<Result<List<daySpecWeather>>>{
        dataSource.getAll()
            .collect{i -> emit(Result.success(i.map { j -> j.toWeeklyWeather() }))}
    }

    override suspend fun insert(date: String, latitude: Double, longitude: Double, temperatureMax: Double, temperatureMin: Double, code: Long) {
        dataSource.insert(
            date = date,
            longitude = longitude,
            latitude = latitude,
            temperatureMax = temperatureMax,
            temperatureMin = temperatureMin,
            code = code)
    }

    override suspend fun deleteAll() {
        dataSource.deleteAll()
    }
}