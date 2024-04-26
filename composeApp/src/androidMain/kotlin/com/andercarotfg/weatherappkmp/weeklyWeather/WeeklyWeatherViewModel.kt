package com.andercarotfg.weatherappkmp.weeklyWeather

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import model.data.weatherBL
import model.domain.dayWeather
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.roundToInt

class WeeklyWeatherViewModel {
    private val _latitude = MutableLiveData<String>()
    val latitude: LiveData<String> = _latitude

    private val _longitude = MutableLiveData<String>()
    val longitude: LiveData<String> = _longitude

    private val _temperatures = MutableLiveData<List<Double>>()
    val temperatures: LiveData<List<Double>> = _temperatures

    private val _maxMin = MutableLiveData<List<Pair<Triple<String, String, String>, Int>>>()
    val maxMin: LiveData<List<Pair<Triple<String, String, String>, Int>>> = _maxMin

    private val _codes = MutableLiveData<List<Int>>()
    val codes: LiveData<List<Int>> = _codes

    private val _hours = MutableLiveData<List<String>>()
    val hours: LiveData<List<String>> = _hours


    private val weekDayList: List<String> = listOf("Lunes", "Martes", "Miercoles", "Jueves",
        "Viernes", "Sábado", "Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes",
        "Sábado")

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAllData(latitude: Double, longitude: Double) {
        val weekW = weatherBL.getAllData(latitude, longitude)
        val dayW = weatherBL.getDailyWeather(weekW)
        val currentHour = LocalDateTime.now()
        val actualDayOfWeek = DayOfWeek.from(currentHour).value
        var lista = emptyList<Pair<Triple<String, String, String>, Int>>().toMutableList()
        println("Dia de la semana: $actualDayOfWeek")
        for (i in 1..7) {
            val currentDay = weatherBL.getSpecificWeekDayTemperature(weekW, i)
            val currentMaxMin = getMaxAndMinT(currentDay)
            val mostRepeatedCode = getAverageCode(currentDay)
            val weekDaySpanish = weekDayList.get((actualDayOfWeek + i)-1)
            val stringFormatted = weekDaySpanish + currentMaxMin
            println(stringFormatted)
            lista.add(Pair(Triple(weekDaySpanish, currentMaxMin.first, currentMaxMin.second), mostRepeatedCode))
        }
        _maxMin.value = lista


    }


    private fun getMaxAndMinT(dayWeather: dayWeather): Pair<String, String> {
        var max = -1
        var min = 1000
        for (t in dayWeather.temperatures) {
            if (t > max) {
                max = t.roundToInt()
            } else if (t<min){
                min = t.roundToInt()
            }
        }
        return Pair(max.toString(), min.toString())
    }

    private fun getAverageCode(dayWeather: dayWeather): Int {
        val conteo = dayWeather.codes.groupingBy { it }.eachCount()
        val numeroMasRepetido = conteo.maxByOrNull { it.value }?.key
        print("Numero mas repetido: $numeroMasRepetido")
        return numeroMasRepetido?.toInt() ?: 0
    }


    fun setLatAndLong(latitude: Double, longitude: Double) {
        _latitude.value = latitude.toString()
        _longitude.value = longitude.toString()
    }
}