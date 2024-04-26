import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.data.weatherAPI
import model.data.weatherBL
import model.domain.weekWeather
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import weatherapppoc_kmp.composeapp.generated.resources.Res
import weatherapppoc_kmp.composeapp.generated.resources.compose_multiplatform

@Preview
@Composable
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }

        LaunchedEffect(Unit){
            println("Hola")
            var weekW = weatherBL.getAllData(43.5667, -5.9 )
            var dayW = weatherBL.getDailyWeather(weekW)
            var actualWeather = weatherBL.getActualTemperature(dayW, 15)
            var dayseven = weatherBL.getSpecificWeekDayTemperature(weekW, 6)
            println(weekW.hourly.temperature_2m)
            println(dayW.temperatures)
            println(actualWeather.temperature)
            println(dayseven.temperatures)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .size(width = 240.dp, height = 100.dp)
        ) {
            Text(
                text = "Elevated",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .size(width = 240.dp, height = 100.dp)
        ) {
            Text(
                text = "Elevated",
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
        }
    }
}


@Preview
@Composable
fun preview(){
    App()
}