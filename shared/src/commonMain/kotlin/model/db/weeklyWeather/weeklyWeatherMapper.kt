package model.db.weeklyWeather

import com.db.DailyWeather
import com.db.WeeklyWeather
import model.domain.daySpecWeather
import model.domain.weekWeather
import model.domain.weeklyWeatherList

fun WeeklyWeather.toWeeklyWeather(): daySpecWeather{
    return daySpecWeather(
        date = date,
        latitude = latitude,
        longitude = longitude,
        temperatureMax = temperatureMax,
        temperatureMin = temperatureMin,
        code = code.toInt(),
    )
}

fun List<daySpecWeather>.toWeeklyWeatherList(): weeklyWeatherList{
    return weeklyWeatherList(
    wekklyList = this
    )
}