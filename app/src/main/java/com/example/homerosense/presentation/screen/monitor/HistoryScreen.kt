package com.example.homerosense.presentation.screen.monitor

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.homerosense.presentation.TempHumidityViewModel
import com.example.homerosense.ui.theme.AppleGray
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

/* Core functionality of the history feature*/
private val values = mutableListOf(Point(0f, 0f))

@Composable
fun HistoryScreen(viewModel: TempHumidityViewModel) {
    Column() {
        val temperatureString = viewModel.temperaturehumidityDate
        var isButtonVisible by remember { mutableStateOf(true) }
        if (isButtonVisible) {
            DisplayList(temperatureString)
        }
        LaunchedEffect(Unit) {
            while(true){
                isButtonVisible = true
                delay(999)
                isButtonVisible = false
                delay(1)
            }
        }

    }
}


@Composable
fun DisplayList(temperatureArray: MutableList<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppleGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .fillMaxHeight()
                .padding(10.dp)
                .background(Color.White, RoundedCornerShape(15.dp)),
        ){
            LazyColumn ( reverseLayout = true,
                state = rememberLazyListState(values.count())
            ) {
                val langx = temperatureArray.takeLast(15)
                items(langx) { language ->
                    Text(language, modifier = Modifier.padding(15.dp))
                    Divider()

                }
            }

        }

    }
}

