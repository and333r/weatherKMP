package model.db.actualWeather

import com.db.ActualWeather
import model.domain.actualWeather

fun ActualWeather.toActualWeather(): actualWeather {
    return actualWeather(
        hour = hour.toInt(),
        latitude = latitude,
        longitude = longitude,
        temperature = temperature,
        humidity = humidity.toInt(),
        code = code.toInt(),
        relativeT = relativeT,
        precipitation = precipitation.toInt()
    )
}