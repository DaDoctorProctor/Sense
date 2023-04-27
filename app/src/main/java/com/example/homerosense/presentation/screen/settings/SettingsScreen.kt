package com.example.homerosense.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.homerosense.presentation.TempHumidityViewModel
import com.example.homerosense.presentation.screen.Screen
import com.example.homerosense.ui.theme.AppleDivider
import com.example.homerosense.ui.theme.AppleGray

private var savedState : Int = 0

@Composable
fun SettingsScreen(viewModel: TempHumidityViewModel = hiltViewModel(),
                   navController: NavController,
    ) {
    Column(
    modifier = Modifier
        .fillMaxSize()
        .background(AppleGray),
    verticalArrangement = Arrangement.Top
    ){
        Box(
            modifier = Modifier
                .width(360.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(15.dp))
                .padding(10.dp)
                .background(Color.White, RoundedCornerShape(15.dp)),
        ){
            Column(
                modifier = Modifier
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Top
            ){

                Title(title = "Sampling time")
                AppleDivider()
                Selector()
            }
        }

        Box(
            modifier = Modifier
                .width(360.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(15.dp))
                .padding(10.dp)
                .background(Color.White, RoundedCornerShape(15.dp)),
        ){
            Column(
                modifier = Modifier
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Top
            ){
                Title(title = "Bluetooth")
                AppleDivider()
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                ){
                    // Restart button
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(
                                vertical = (10.dp),
                                horizontal = (10.dp),
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xB2D7D7D7), RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.closeConnection()
                                viewModel.disconnect()
                                viewModel.initializeConnection()
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Reconnect",
                            color = Color.Black,
                            modifier = Modifier.
                            padding(
                                vertical = 12.dp,
                                horizontal = 18.dp,
                            )
                        )
                    }
                    // Disconnect button
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(
                                vertical = (10.dp),
                                horizontal = (10.dp),
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xB2D7D7D7), RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.closeConnection()
                                viewModel.disconnect()
                                navController.navigate(Screen.StartScreen.route) {
                                    popUpTo(Screen.TemperatureHumidityScreen.route) {
                                        inclusive = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "Disconnect",
                            color = Color.Black,
                            modifier = Modifier.
                            padding(
                                vertical = 12.dp,
                                horizontal = 18.dp,
                            )
                        )
                    }

                }
            }
        }





    }
}

@Composable
fun Title(title : String){
    Text(
        text = title,
        fontSize = 15.sp,
        modifier = Modifier
            .padding(
                vertical = 10.dp,
                horizontal = 10.dp,
            )
    )
}


@Composable
fun Selector(viewModel: TempHumidityViewModel = hiltViewModel()){
    val states = listOf("1s", "10s", "30s",
        "1m", "5m")
    var selectedOption by remember {
        mutableStateOf(states[savedState])
    }
    val onSelectionChange = { text: String ->
        selectedOption = text
    }
    Surface(
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        modifier = Modifier
            .wrapContentSize()
            .padding(
                vertical = (10.dp),
                horizontal = (10.dp),
            )
    ) {
        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(16.dp))
                .background(Color(0xB2D7D7D7))
                .padding(
                    vertical = (5.dp),
                    horizontal = (5.dp),
                )
        ) {
            states.forEach { text->
                Text(
                    text = text,
                    color =
                    if (text == selectedOption) {
                        Color.White
                    } else {
                        Color.Black
                    },
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(16.dp))
                        .clickable {
                            onSelectionChange(text)
                            when (selectedOption) {
                                states[0] -> {
                                    viewModel.makeRequest("T0")
                                    savedState = 0
                                }
                                states[1] -> {
                                    viewModel.makeRequest("T1")
                                    savedState = 1
                                }
                                states[2] -> {
                                    viewModel.makeRequest("T2")
                                    savedState = 2
                                }
                                states[3] -> {
                                    viewModel.makeRequest("T3")
                                    savedState = 3
                                }
                                states[4] -> {
                                    viewModel.makeRequest("T4")
                                    savedState = 4
                                }
                            }
                        }
                        .background(
                            if (text == selectedOption) {
                                Color(0xFF709FD7)
                            } else {
                                Color.White
                            }
                        )
                        .padding(
                            vertical = 12.dp,
                            horizontal = 18.dp,
                        ),
                )
            }
        }
    }
}