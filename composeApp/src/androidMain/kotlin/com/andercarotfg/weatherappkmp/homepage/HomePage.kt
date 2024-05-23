package com.andercarotfg.weatherappkmp.homepage

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andercarotfg.weatherappkmp.actualWeather.actualWeather
import ui.actualWeather.ActualWeatherViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun My_App(actualWeatherViewModel: ActualWeatherViewModel) {
    MaterialTheme {
        Column {
            //header()
            actualWeather(actualWeatherViewModel)
            dailyWeather(actualWeatherViewModel)
            weeklyWeather(actualWeatherViewModel)
        }
    }
}

@Composable
fun header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "FORECASTOPOLIS",
            style = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                fontSize = 15.sp
            )
        )
    }
}




@Composable
fun weeklyWeather(actualWeatherViewModel: ActualWeatherViewModel) {
    Text(text = "TODO")
}

@Composable
fun dailyWeather(actualWeatherViewModel: ActualWeatherViewModel) {
    Text(text = "TODO")
}