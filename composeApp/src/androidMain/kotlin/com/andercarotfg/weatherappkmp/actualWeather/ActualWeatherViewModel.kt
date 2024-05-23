package ui.actualWeather

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import app.cash.sqldelight.db.AfterVersion
import com.db.WeatherAppDatabaseKMP
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import model.data.weatherBL
import model.db.DatabaseDriverFactory
import model.db.createDatabase
import model.db.actualWeather.actualWeatherDataSource
import model.db.actualWeather.actualWeatherRepository
import model.db.actualWeather.actualWeatherRepositorySQL
import kotlin.math.roundToInt
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class ActualWeatherViewModel : ViewModel(){
    val color_init = listOf(
        Color(0xFF000000),
        Color(0xFF212121),
        Color(0xFF424242),
        Color(0xFF616161),
        Color(0xFF9E9E9E)
    )

    private val _actualT = MutableStateFlow<String>("0")
    val actualT : StateFlow<String> = _actualT

    private val _actualC = MutableStateFlow<String>("0")
    val actualC : StateFlow<String> = _actualC

    private val _actualH = MutableStateFlow<String>("Humedad: -%")
    val actualH : StateFlow<String> = _actualH

    private val _actualRT = MutableStateFlow<String>("Sensación térmica: -ºC")
    val actualRT : StateFlow<String> = _actualRT

    private val _actualP = MutableStateFlow<String>("Precipitaciones: -%")
    val actualP : StateFlow<String> = _actualP

    private val _estado = MutableStateFlow<String>("-")
    val estado : StateFlow<String> = _estado

    private val _gradientColorList = MutableStateFlow<List<Color>>(color_init)
    val gradientColorList : StateFlow<List<Color>> = _gradientColorList

    private val _latitude = MutableStateFlow<String>("43.5667")
    val latitude : StateFlow<String> = _latitude

    private val _longitude = MutableStateFlow<String>("-5.9")
    val longitude : StateFlow<String> = _longitude

    val db = createDatabase(DatabaseDriverFactory())
    val ds_aw = actualWeatherDataSource(db)
    val repo_aw = actualWeatherRepositorySQL(dataSource =  ds_aw)

    suspend fun getAllData(latitude: Double, longitude: Double) {
        val weekW = weatherBL.getAllData(_latitude.value.toDouble(), _longitude.value.toDouble())
        val currentHour = Clock.System.now()
        val currentTime = currentHour.toLocalDateTime(TimeZone.UTC).hour
        val res = repo_aw.getAll()

        res.collect{
            val lastWeather = res.first()
            lastWeather.onSuccess {
                val values = it.firstOrNull()
                var cambio = false
                if(it.isNotEmpty()){
                    if(latitude == values?.latitude || longitude==values?.longitude){
                        val ultimaHora = values.hour
                        if(ultimaHora != (currentTime+2)){
                            cambio = true
                        }
                    }else{
                        cambio = true
                    }
                    _gradientColorList.value = returnGradient(values!!.code)
                    _actualT.value = values.temperature.roundToInt().toString() + "º"
                    _actualC.value = values.code.toString()
                    var aux = values.humidity.toString()
                    _actualH.value = "Humedad: $aux%"
                    aux = values.relativeT.roundToInt().toString()
                    _actualRT.value = "Sensación térmica: $aux" + "º"
                    aux = values.precipitation.toString()
                    _actualP.value = "Precipitaciones: $aux%"
                    _estado.value = weatherBL.returnEstado(_actualC.value.toInt())
                }else{
                    cambio = true
                }
                if(cambio) {
                    val dayW = weatherBL.getDailyWeather(weekW)
                    val actualWeather = weatherBL.getActualTemperature(dayW, currentTime + 1)
                    val dayseven = weatherBL.getSpecificWeekDayTemperature(weekW, 6)
                    var aux = actualWeather.temperature.roundToInt().toString()
                    repo_aw.deleteAll()
                    repo_aw.insert(
                        currentTime.toLong() + 2, latitude, longitude,
                        actualWeather.temperature,
                        actualWeather.humidity.toLong(),
                        actualWeather.code.toLong(),
                        actualWeather.relativeT,
                        actualWeather.precipitation.toLong()
                    )
                }


            }.onFailure {
            }
        }
    }
    fun returnGradient(code: Int): List<Color> {
        val indexSunny = listOf(0, 51, 53, 55, 56, 57)
        val gradientColorListSunny = listOf(
            Color(0xFFFFF176),
            Color(0xFFFFEE58),
            Color(0xFFFFEB3B),
            Color(0xFFFFD600),
            Color(0xFFFFC107),

            )
        val gradientColorListNight = listOf(
            Color(0xFF000000),
            Color(0xFF212121),
            Color(0xFF424242),
            Color(0xFF616161),
            Color(0xFF9E9E9E)
        )

        val indexRain = listOf(61, 63, 65, 80, 81, 82)
        val gradientColorListCloudy = listOf(
            Color(0xFF1565C0),
            Color(0xFF1976D2),
            Color(0xFF1E88E5),
            Color(0xFF2196F3),
            Color(0xFF64B5F6)
        )
        val indexStorm = listOf(95, 96, 99)
        val gradientColorListStorm = listOf(
            Color(0xFF212121),
            Color(0xFF455A64),
            Color(0xFF607D8B),
            Color(0xFF78909C),
            Color(0xFF90A4AE)
        )
        val indexSnow = listOf(71, 73, 75, 77, 85, 86)
        val gradientColorListSnow = listOf(
            Color(0xFFE0E0E0),
            Color(0xFFBDBDBD),
            Color(0xFF9E9E9E),
            Color(0xFF757575),
            Color(0xFF424242)
        )
        val indexCloudyWithSun = listOf(1, 2)
        val gradientColorListCloudyWithSun = listOf(
            Color(0xFF81D4FA),
            Color(0xFF4FC3F7),
            Color(0xFF29B6F6),
            Color(0xFF03A9F4),
            Color(0xFF039BE5)
        )
        val indexCloudy = listOf(3, 45, 48)
        val gradientColorListRain = listOf(
            Color(0xFFB0BEC5),
            Color(0xFF90A4AE),
            Color(0xFF78909C),
            Color(0xFF607D8B),
            Color(0xFF455A64)
        )
        return if (indexSunny.contains(code)) {
            gradientColorListSunny
        } else if (indexRain.contains(code)) {
            gradientColorListRain
        } else if (indexStorm.contains(code)) {
            gradientColorListStorm
        } else if (indexSnow.contains(code)) {
            gradientColorListSnow
        } else if (indexCloudyWithSun.contains(code)) {
            gradientColorListCloudyWithSun
        } else if (indexCloudy.contains(code)) {
            gradientColorListCloudy
        } else {
            gradientColorListNight
        }
    }

    fun setLatAndLong(latitude: Double, longitude: Double){
        println("He pasado por setLatAndLong")
        println(latitude)
        println(longitude)

        _latitude.value = latitude.toString()
        _longitude.value = longitude.toString()

    }



}