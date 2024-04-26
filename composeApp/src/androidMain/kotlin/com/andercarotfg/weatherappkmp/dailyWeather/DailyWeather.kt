package com.andercarotfg.weatherappkmp.dailyWeather

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andercarotfg.weatherappkmp.R
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun dailyWeather (dailyWeatherViewModel: DailyWeatherViewModel){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .horizontalScroll(rememberScrollState()
            )
    ){
        val initListD = listOf(1.0, 2.0, 3.0)
        val initListI = listOf(1, 2, 3)
        val initListS = listOf("1", "2", "3")

        val latitude : String by dailyWeatherViewModel.latitude.observeAsState(initial = "43.5667")
        val longitude : String by dailyWeatherViewModel.longitude.observeAsState(initial = "-5.9")
        LaunchedEffect(latitude) {
            println("Hola")
            dailyWeatherViewModel.getAllData(latitude.toDouble(), longitude.toDouble())
            println(latitude)
            println(longitude)
        }

        val temperatures : List<Double> by dailyWeatherViewModel.temperatures.observeAsState(initial = initListD)
        val codes : List<Int> by dailyWeatherViewModel.codes.observeAsState(initial = initListI)
        val hours : List<String> by dailyWeatherViewModel.hours.observeAsState(initial = initListS)

        Spacer(modifier = Modifier.height(6.dp))
        Row (modifier = Modifier.fillMaxWidth()) {
            repeat(temperatures.size){
                Card (modifier = Modifier.padding(8.dp).size(124.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize() ){
                        val imagePainter = returnImagePainter(codes[it], hours[it])
                        Spacer(modifier = Modifier.height(8.dp)) // Agrega un espacio entre la imagen y el texto de la temperatura
                        if(it == 0){
                            Text(
                                text = "Actualmente",
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            )
                        } else {
                            Text(
                                text = hours[it] + ":00",
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            )
                        }

                        Text(
                            text = temperatures[it].roundToInt().toString() + "ºC",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            )
                        )
                        Image(
                            painter = imagePainter,
                            contentDescription = "Descripción de la imagen",
                            modifier = Modifier.scale(0.75F),
                        )
                    }

                }
            }
        }

    }
}

@Composable
fun returnImagePainter(code: Int, s: String): Painter {
    val indexSunny = listOf(0, 51, 53, 55, 56, 57)
    val indexRain = listOf(61, 63, 65, 80, 81, 82)
    val indexStorm = listOf(95, 96, 99)
    val indexSnow = listOf(71, 73, 75, 77, 85, 86)
    val indexCloudyWithSun = listOf(1, 2)
    val indexCloudy = listOf(3, 45, 48)
    return if (indexSunny.contains(code)) {
        if(s.toInt() > 19){
            painterResource(id = R.drawable.de_noche)
        } else{
            painterResource(id = R.drawable.soleado)
        }
    } else if (indexRain.contains(code)) {
        painterResource(id = R.drawable.lluvia)
    } else if (indexStorm.contains(code)) {
        painterResource(id = R.drawable.tormenta)
    } else if (indexSnow.contains(code)) {
        painterResource(id = R.drawable.nieve)
    } else if (indexCloudyWithSun.contains(code)) {
        painterResource(id = R.drawable.nublado_con_sol)
    } else if (indexCloudy.contains(code)) {
        painterResource(id = R.drawable.nublado)
    } else {
        painterResource(id = R.drawable.de_noche)
    }
}

@Composable
private fun GradientBackgroundBrush(isVerticalGradient: Boolean, colors: List<Color>): Brush {
    val endOffSet = if (isVerticalGradient) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }
    return Brush.linearGradient(
        colors = colors,
        start = Offset.Zero,
        end = endOffSet
    )
}