package model.db.dailyWeather

import com.db.DailyWeather
import model.domain.hourlyWeather

fun DailyWeather.toDailyWeather(): hourlyWeather {
    return hourlyWeather(
        date = date,
        latitude = latitude,
        longitude = longitude,
        temperature = temperature,
        code = code.toInt(),
    )
}