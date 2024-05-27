package model.db.actualWeather

import kotlinx.coroutines.flow.flow
import model.domain.actualWeather
class actualWeatherRepositorySQL(
    private val dataSource: ActualDataSource
): actualWeatherRepository {

    override fun getAll() = flow<Result<List<actualWeather>>>{
        dataSource.getAll()
            .collect{i -> emit(Result.success(i.map { j -> j.toActualWeather() }))}
    }

    suspend fun getAlliOS() = flow<actualWeather?>{
        getAll().collect{ i -> i.onSuccess { j -> emit(j.last())}}
    }

    override suspend fun insert(hour: Long, latitude: Double, longitude: Double, temperature: Double, humidity: Long, code: Long, relativeT: Double, precipitation: Long) {
       dataSource.insert(
           hour = hour,
           longitude = longitude,
           latitude = latitude,
           temperature = temperature,
           humidity = humidity,
           code = code,
           relativeT = relativeT,
           precipitation = precipitation)
    }

    override suspend fun deleteAll() {
        dataSource.deleteAll()
    }
}