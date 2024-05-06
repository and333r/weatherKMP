package model.data

import io.ktor.client.call.body
import io.ktor.client.request.get
import model.domain.weekWeather
import kotlinx.serialization.json.Json
import model.domain.actualWeather
import model.domain.dayWeather


class weatherBL {
        suspend fun getAllData(latitude: Double, longitude: Double): weekWeather {
            val URL = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation_probability,weather_code&timezone=Europe%2FBerlin"
            println("Retrieving data...")
            val data = weatherAPI.httpClient.get(URL).body<weekWeather>()
            println("Data retrieved...")
            return data
        }

        fun getDailyWeather(weekWeather: weekWeather): dayWeather{
            println("Retrieving daily data...")
            return dayWeather(weekWeather.latitude, weekWeather.longitude,
                weekWeather.hourly.temperature_2m.take(24),
                weekWeather.hourly.relative_humidity_2m.take(24),
                weekWeather.hourly.weather_code.take(24),
                weekWeather.hourly.apparent_temperature.take(24),
                weekWeather.hourly.precipitation_probability.take(24)
            )
        }

        fun getActualTemperature(dayWeather: dayWeather, hour: Int): actualWeather{
            println("Retrieving actual data...")
            return actualWeather(dayWeather.latitude, dayWeather.longitude,
                dayWeather.temperatures[hour],
                dayWeather.humidities[hour],
                dayWeather.codes[hour],
                dayWeather.relativeTs[hour],
                dayWeather.precipitations[hour]
            )
        }

        fun getSpecificWeekDayTemperature(weekWeather: weekWeather, dayNumber: Int): dayWeather{
            println("Retrieving each days data...")
            return dayWeather(weekWeather.latitude, weekWeather.longitude,
                weekWeather.hourly.temperature_2m.subList((24*(dayNumber-1)), 24*dayNumber),
                weekWeather.hourly.relative_humidity_2m.subList((24*(dayNumber-1)), 24*dayNumber),
                weekWeather.hourly.weather_code.subList((24*(dayNumber-1)), 24*dayNumber),
                weekWeather.hourly.apparent_temperature.subList((24*(dayNumber-1)), 24*dayNumber),
                weekWeather.hourly.precipitation_probability.subList((24*(dayNumber-1)), 24*dayNumber)
            )
        }

        fun returnEstado(code: Int): String {
            val indexSunny = listOf(0, 51, 53, 55, 56, 57)
            val indexRain = listOf(61, 63, 65, 80, 81, 82)
            val indexStorm = listOf(95, 96, 99)
            val indexSnow = listOf(71, 73, 75, 77, 85, 86)
            val indexCloudyWithSun = listOf(1, 2)
            val indexCloudy = listOf(3, 45, 48)

            return if (indexSunny.contains(code)) {
                "Soleado"
            } else if (indexRain.contains(code)) {
                "Lluvioso"
            } else if (indexStorm.contains(code)) {
                "Tormenta"
            } else if (indexSnow.contains(code)) {
                "Nevando"
            } else if (indexCloudyWithSun.contains(code)) {
                "Nuboso"
            } else if (indexCloudy.contains(code)) {
                "Nublado"
            } else {
                "Noche"
            }
        }


    }