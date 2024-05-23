package ui.dailyWeather

import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.data.weatherBL
import model.db.DatabaseDriverFactory
import model.db.actualWeather.actualWeatherDataSource
import model.db.actualWeather.actualWeatherRepositorySQL
import model.db.actualWeather.dailyWeatherRepositorySQL
import model.db.createDatabase
import model.db.dailyWeather.dailyWeatherDataSource
import kotlin.math.roundToInt


class DailyWeatherViewModel {

    private val initListD =
        listOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0)
    private val initListI = listOf(1, 2, 3, 2, 3, 1, 3, 3, 3, 23, 1, 2, 3)
    private val initListS =
        listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13")

    private val _latitude = MutableStateFlow<String>("43.5667")
    val latitude: StateFlow<String> = _latitude

    private val _longitude = MutableStateFlow<String>("-5.9")
    val longitude: StateFlow<String> = _longitude

    private val _temperatures = MutableStateFlow<List<Double>>(initListD)
    val temperatures: StateFlow<List<Double>> = _temperatures

    private val _codes = MutableStateFlow<List<Int>>(initListI)
    val codes: StateFlow<List<Int>> = _codes

    private val _hours = MutableStateFlow<List<String>>(initListS)
    val hours: StateFlow<List<String>> = _hours

    val db = createDatabase(DatabaseDriverFactory())
    val ds_dw = dailyWeatherDataSource(db)
    val repo_dw = dailyWeatherRepositorySQL(dataSource = ds_dw)


    suspend fun getAllData(latitude: Double, longitude: Double) {
        val currentHour = Clock.System.now()
        var currentTime = currentHour.toLocalDateTime(TimeZone.UTC).hour
        currentTime += 2
        val res = repo_dw.getAll()
        var cambio: Boolean
        val weekW = weatherBL.getAllData(_latitude.value.toDouble(), _longitude.value.toDouble())
        val dayW = weatherBL.getDailyWeather(weekW)

        res.collect { dailyWeatherResult ->
            cambio = false
            dailyWeatherResult.onSuccess { hw ->
                if(hw.isNotEmpty()){
                    if (hw.first().longitude == _longitude.value.toDouble() || hw.first().latitude == _latitude.value.toDouble()) {
                        if (hw.first().date.toInt() != currentTime) {
                            cambio = true
                        }
                    } else {
                        cambio = true
                    }

                    var horas: MutableList<String> = mutableListOf()
                    var temperatures: MutableList<Double> = mutableListOf()
                    var codes: MutableList<Int> = mutableListOf()
                    for(h in hw){
                        horas.add(h.date)
                        temperatures.add(h.temperature)
                        codes.add(h.code)
                    }
                    _hours.value = horas
                    _temperatures.value = temperatures
                    _codes.value = codes


                }else{
                    cambio = true
                }
                if (cambio) {
                    val range = currentTime..23
                    val aux_list = dayW.temperatures.drop(currentTime)
                    val aux_list2 = dayW.codes.drop(currentTime)
                    repo_dw.deleteAll()
                    for (h in range) {
                        repo_dw.insert(
                            h.toString(),
                            latitude,
                            longitude,
                            aux_list[h - currentTime],
                            aux_list2[h - currentTime].toLong()
                        )
                    }
                    var horas: MutableList<String> = mutableListOf()
                    var temperatures: MutableList<Double> = mutableListOf()
                    var codes: MutableList<Int> = mutableListOf()
                    for(h in hw){
                        horas.add(h.date)
                        temperatures.add(h.temperature)
                        codes.add(h.code)
                    }
                    _hours.value = horas
                    _temperatures.value = temperatures
                    _codes.value = codes
                }

            }.onFailure {

            }


        }


    }
    fun setLatAndLong(latitude: Double, longitude: Double) {
        _latitude.value = latitude.toString()
        _longitude.value = longitude.toString()
    }
}