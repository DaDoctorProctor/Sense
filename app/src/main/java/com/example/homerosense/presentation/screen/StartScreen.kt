package com.example.homerosense.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun StartScreen(
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        /* Welcome label */
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column() {
                Text(
                    text = "Welcome!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
                Text(
                    text = "Press connect to start",
                    fontSize = 20.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Connect",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF709FD7),
                    modifier = Modifier.clickable{
                        navController.navigate(Screen.BluetoothLoadingScreen.route) {
                            popUpTo(Screen.StartScreen.route) {
                                inclusive = true
                            }
                        }

                    }
                )
            }
        }
    }

}






