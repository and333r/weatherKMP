package com.andercarotfg.weatherappkmp.dailyWeather

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import model.data.weatherBL
import java.time.LocalTime
import kotlin.math.roundToInt

class DailyWeatherViewModel {
    private val _latitude = MutableLiveData<String>()
    val latitude : LiveData<String> = _latitude

    private val _longitude = MutableLiveData<String>()
    val longitude : LiveData<String> = _longitude

    private val _temperatures = MutableLiveData<List<Double>>()
    val temperatures : LiveData<List<Double>> = _temperatures

    private val _codes = MutableLiveData<List<Int>>()
    val codes : LiveData<List<Int>> = _codes

    private val _hours = MutableLiveData<List<String>>()
    val hours : LiveData<List<String>> = _hours


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAllData(latitude: Double, longitude: Double) {
        val weekW = weatherBL.getAllData(latitude, longitude)
        val dayW = weatherBL.getDailyWeather(weekW)
        val currentHour = LocalTime.now().hour
        val range = currentHour..23
        val my_array = range.toList().toTypedArray()
        val final_array: MutableList<String> = my_array.map { it.toString() }.toMutableList()
        val test1 = final_array
        val test2 = dayW.temperatures.drop(currentHour)
        val test3 = dayW.codes.drop(currentHour)
        _hours.value = final_array
        _temperatures.value = dayW.temperatures.drop(currentHour)
        _codes.value = dayW.codes.drop(currentHour)
        println(test1)
        println(test2)
        println(test3)


    }



    fun setLatAndLong(latitude: Double, longitude: Double){
        _latitude.value = latitude.toString()
        _longitude.value = longitude.toString()
    }
}