package model.db.weeklyWeather

import com.db.DailyWeather
import com.db.WeeklyWeather
import model.domain.daySpecWeather

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