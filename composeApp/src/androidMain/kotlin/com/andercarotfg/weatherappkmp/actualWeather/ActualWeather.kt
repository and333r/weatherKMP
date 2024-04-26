package com.andercarotfg.weatherappkmp.actualWeather

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun actualWeather(actualWeatherViewModel: ActualWeatherViewModel, modifier: Modifier = Modifier) {
    val actualT : String by actualWeatherViewModel.actualT.observeAsState(initial = "14º")
    val actualC : String by actualWeatherViewModel.actualC.observeAsState(initial = "23")
    val actualH : String by actualWeatherViewModel.actualH.observeAsState(initial = "Humedad: ")
    val actualRT : String by actualWeatherViewModel.actualRT.observeAsState(initial = "Sensación térmica: ºC")
    val actualP : String by actualWeatherViewModel.actualP.observeAsState(initial = "Precipitaciones: ")
    val estado : String by actualWeatherViewModel.estado.observeAsState(initial = "Noche")
    val latitude : String by actualWeatherViewModel.latitude.observeAsState(initial = "43.5667")
    val longitude : String by actualWeatherViewModel.longitude.observeAsState(initial = "-5.9")

    LaunchedEffect(latitude) {
        println("Hola")
        actualWeatherViewModel.getAllData(latitude.toDouble(), longitude.toDouble())
        println(latitude)
        println(longitude)
    }

    actualWeatherInfo(actualH, actualT, actualP, actualRT, actualC.toInt(), estado, actualWeatherViewModel)

}




@Composable
fun actualWeatherInfo(
    actualH: String,
    actualT: String,
    actualP: String,
    actualRT: String,
    actualC: Int,
    estado: String,
    actualWeatherViewModel: ActualWeatherViewModel
) {
    val color_init = listOf(
        Color(0xFF000000),
        Color(0xFF212121),
        Color(0xFF424242),
        Color(0xFF616161),
        Color(0xFF9E9E9E)
    )
    val gradientColorList : List<Color> by actualWeatherViewModel.gradientColorList.observeAsState(initial = color_init)
    actualWeatherViewModel.returnGradient(actualC)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
            .background(
                brush = GradientBackgroundBrush(
                    isVerticalGradient = false,
                    colors = gradientColorList
                )
            )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Column {
                Spacer(modifier = Modifier.width(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Text(
                            text = "Tª en tu ubicación:",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = actualT,
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 96.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = estado,
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        var imagePainter = returnImagePainter(actualC)
                        Image(
                            painter = imagePainter,
                            contentDescription = "Descripción de la imagen",
                            modifier = Modifier.scale(1.5F),
                        )
                        Column(horizontalAlignment = Alignment.Start) {
                            Spacer(modifier = Modifier.height(50.dp))
                            Text(
                                text = actualH,
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            )
                            Text(
                                text = actualRT,
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            )
                            Text(
                                text = actualP,
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    thickness = 2.dp,
                    color = Color.Black
                )
            }
        }

    }
}


@Composable
fun returnImagePainter(code: Int): Painter {
    val indexSunny = listOf(0, 51, 53, 55, 56, 57)
    val indexRain = listOf(61, 63, 65, 80, 81, 82)
    val indexStorm = listOf(95, 96, 99)
    val indexSnow = listOf(71, 73, 75, 77, 85, 86)
    val indexCloudyWithSun = listOf(1, 2)
    val indexCloudy = listOf(3, 45, 48)

    return if (indexSunny.contains(code)) {
        painterResource(id = R.drawable.soleado)
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