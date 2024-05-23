package ui.weeklyWeather

import com.db.WeatherAppDatabaseKMP
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.data.weatherBL
import model.db.DatabaseDriverFactory
import model.db.createDatabase
import model.db.actualWeather.actualWeatherDataSource
import model.db.actualWeather.actualWeatherRepositorySQL
import model.db.actualWeather.dailyWeatherRepositorySQL
import model.db.actualWeather.weeklyWeatherRepositorySQL
import model.db.dailyWeather.dailyWeatherDataSource
import model.db.weeklyWeather.weeklyWeatherDataSource
import model.domain.daySpecWeather

class WeeklyWeatherViewModel {

    val initListMaxMin = listOf(
        Pair(Triple("Sábado", "14", "9.3"), 0),
        Pair(Triple("Domingo", "14", "9.3"), 0),
        Pair(Triple("Lunes", "14", "9.3"), 0),
        Pair(Triple("Martes", "14", "9.3"), 0),
        Pair(Triple("Miercoles", "14", "9.3"), 0),
        Pair(Triple("Jueves", "14", "9.3"), 0),
        Pair(Triple("Viernes", "14", "9.3"), 0)
    )

    private val initListI = listOf(1, 2, 3, 2, 3, 1, 3)
    private val initListS = listOf("1", "2", "3", "4", "5", "6", "7")



    private val _latitude = MutableStateFlow<String>("43.5667")
    val latitude: StateFlow<String> = _latitude

    private val _longitude = MutableStateFlow<String>("-5.9")
    val longitude: StateFlow<String> = _longitude

    //private val _temperatures = MutableStateFlow<List<Double>>()
    //val temperatures: StateFlow<List<Double>> = _temperatures

    private val _maxMin = MutableStateFlow<List<Pair<Triple<String, String, String>, Int>>>(initListMaxMin)
    val maxMin: StateFlow<List<Pair<Triple<String, String, String>, Int>>> = _maxMin

    private val _codes = MutableStateFlow<List<Int>>(initListI)
    val codes: StateFlow<List<Int>> = _codes

    private val _hours = MutableStateFlow<List<String>>(initListS)
    val hours: StateFlow<List<String>> = _hours



    private val weekDayList: List<String> = listOf("Lunes", "Martes", "Miercoles", "Jueves",
        "Viernes", "Sábado", "Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes",
        "Sábado")

    val db = createDatabase(DatabaseDriverFactory())
    val ds_ww = weeklyWeatherDataSource(db)
    val repo_ww = weeklyWeatherRepositorySQL(dataSource = ds_ww)
    suspend fun getAllData(latitude: Double, longitude: Double) {
        val weekW = weatherBL.getAllData(_latitude.value.toDouble(), _longitude.value.toDouble())
        val currentHour = Clock.System.now()
        val currentTime = currentHour.toLocalDateTime(TimeZone.UTC).dayOfWeek.ordinal
        val weekDaySpanish = weekDayList.get((currentTime + 1)-1)
        val res = repo_ww.getAll()

        res.collect { weeklyWeatherResult ->
            weeklyWeatherResult.onSuccess { ww ->
                var cambio = false
                if(ww.isNotEmpty()){
                    if (ww.first().longitude == _longitude.value.toDouble() || ww.first().latitude == _latitude.value.toDouble()) {
                        if (ww.first().date != weekDaySpanish) {
                            cambio = true
                            println("ANDER")
                        }
                    } else {
                        cambio = true
                        println("MIKEl")
                    }
                    var data: MutableList<Pair<Triple<String, String, String>, Int>> = mutableListOf()
                    for(i in ww){
                        data.add(Pair(Triple(i.date, i.temperatureMax.toString(), i.temperatureMin.toString()), i.code))
                    }
                    _maxMin.value = data

                }else{
                    cambio = true
                    println("NAIA")
                }
                if (cambio) {
                    repo_ww.deleteAll()
                    for (i in 1..7) {
                        val currentDay = weatherBL.getSpecificWeekDayTemperature(weekW, i)
                        val currentMaxMin = weatherBL.getMaxAndMinT(currentDay)
                        val mostRepeatedCode = weatherBL.getAverageCode(currentDay)
                        val weekDaySpanish = weekDayList.get((currentTime + i)-1)
                        repo_ww.insert(weekDaySpanish, _latitude.value.toDouble(),
                            _longitude.value.toDouble(),
                            currentMaxMin.first.toDouble(),
                            currentMaxMin.second.toDouble(), mostRepeatedCode.toLong())
                    }
                    var data: MutableList<Pair<Triple<String, String, String>, Int>> = mutableListOf()
                    for(i in ww){
                        data.add(Pair(Triple(i.date, i.temperatureMax.toString(), i.temperatureMin.toString()), i.code))
                    }
                    _maxMin.value = data
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