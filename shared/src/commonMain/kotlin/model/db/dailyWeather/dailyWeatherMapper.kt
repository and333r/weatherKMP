package model.db.dailyWeather

import com.db.DailyWeather
import kotlinx.coroutines.joinAll
import model.domain.hourlyWeather
import model.domain.hourlyWeatherList

fun DailyWeather.toDailyWeather(): hourlyWeather {
    return hourlyWeather(
        date = date,
        latitude = latitude,
        longitude = longitude,
        temperature = temperature,
        code = code.toInt(),
    )
}

fun List<hourlyWeather>.toHourlyWeatherList(): hourlyWeatherList{
    return hourlyWeatherList(
        dailyList = this
    )
}