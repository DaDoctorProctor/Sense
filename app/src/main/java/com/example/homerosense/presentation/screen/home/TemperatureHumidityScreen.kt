package com.example.homerosense.presentation.screen.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.homerosense.data.ConnectionState
import com.example.homerosense.presentation.TempHumidityViewModel
import com.example.homerosense.ui.theme.AppleDivider
import com.example.homerosense.ui.theme.AppleGray
import com.example.homerosense.ui.theme.AppleRed
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay

@Composable
fun TemperatureHumidityScreen(
    onBluetoothStateChanged: () -> Unit,
    viewModel: TempHumidityViewModel
) {
    Column(modifier = Modifier
        .background(color = AppleGray)
        .fillMaxSize()) {

        Box(contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .padding(10.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(color = Color.White, RoundedCornerShape(15.dp)))
        { GoodMorning(viewModel)}

        Box(contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .padding(10.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(color = Color.White, RoundedCornerShape(15.dp)))
        { Summary()}

        Box(contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .padding(10.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .background(color = Color.White, RoundedCornerShape(15.dp)))
        { ConnectionStatus(viewModel)}


    }
}

@Composable

fun GoodMorning(viewModel: TempHumidityViewModel){
    Column() {
        Text(text = "Good morning", fontWeight = FontWeight.Bold, fontSize = 30.sp,
            modifier = Modifier.padding(10.dp))
        AppleDivider()
        Text(text = "The current sensor's temperature is ${viewModel.temperature} C " +
                "with a humidity of: ${viewModel.humidity} %",
            fontWeight = FontWeight.Normal, fontSize = 15.sp,
            modifier = Modifier.padding(10.dp))
    }
}

@Composable
fun Summary(){
    Column() {
        Text(text = "Summary", fontWeight = FontWeight.Normal, fontSize = 20.sp,
            modifier = Modifier.padding(10.dp))
        AppleDivider()
        Text(text = "The statistics show a max temperature of 40 C showing an increase of 10 % " +
                "over yesterday.",
            fontWeight = FontWeight.Normal, fontSize = 15.sp,
            modifier = Modifier.padding(10.dp))
    }
}

@Composable
fun ConnectionStatus(viewModel : TempHumidityViewModel){
    Column() {
        Text(text = "Connection status", fontWeight = FontWeight.Normal, fontSize = 20.sp,
            modifier = Modifier.padding(10.dp))
        AppleDivider()

        var status by remember { mutableStateOf("") }
        var color by remember { mutableStateOf(Color) }

        when (viewModel.connectionState) {
            ConnectionState.Disconnected -> {
                status = "Disconnected"
            }
            ConnectionState.Connected -> {
                status = "Connected"
            }
            ConnectionState.CurrentlyInitializing -> {
                status = "Initializing"
            }
            else -> {}
        }

        Text(text = status,
            fontWeight = FontWeight.Bold, fontSize = 15.sp,
            modifier = Modifier.padding(10.dp),
            color = when (status) {
                "Connected" -> {
                    Color.Green
                }
                "Initializing" -> {
                    Color.Yellow
                }
                else -> {
                    AppleRed
                }
            }
        )

    }
}















