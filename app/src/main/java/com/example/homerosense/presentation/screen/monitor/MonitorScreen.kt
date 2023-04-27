package com.example.homerosense.presentation.screen.monitor

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.homerosense.presentation.TempHumidityViewModel
import com.example.homerosense.presentation.screen.Screen
import com.example.homerosense.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

data class Point(val x: Float, val y: Float)

@Composable
fun MonitorScreen(viewModel: TempHumidityViewModel,
                  navController: NavController) {
    // our values to draw
    Column(
        modifier = Modifier
            .fillMaxSize().verticalScroll(rememberScrollState())
            .background(AppleGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Chart(viewModel = viewModel)

        // History Button
        ButtonList(navController = navController)

    } /*End column*/

}




@Composable
fun ButtonList(navController: NavController){
    Column(
        modifier = Modifier
            .height(900.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color.White, RoundedCornerShape(15.dp)),
        ){
            Column() {

                AppleItem(navController = navController, name = "History", color = AppleBlue2,
                    icon = Icons.Default.Send, screen = Screen.HistoryScreen)
                AppleDivider()
                AppleItem(navController = navController, name = "Statistics",color = AppleRed,
                    icon = Icons.Default.Share, screen = Screen.StatisticsScreen)
                AppleDivider()
                AppleItem(navController = navController, name = "Ideas 1",color = AppleRed,
                    icon = Icons.Default.Favorite, screen = Screen.Ideas)
                AppleDivider()
                AppleItem(navController = navController, name = "Ideas 2",color = AppleRed,
                    icon = Icons.Default.Favorite, screen = Screen.Ideas2)

            }

        }
    }
}

@Composable
fun AppleItem(navController: NavController, name: String,
               color : Color, icon : ImageVector, screen: Screen){
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White, RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .height(40.dp)
            .clickable {
                navController.navigate(screen.route) {
                    popUpTo(Screen.Monitor.route) {
                        inclusive = false
                    }
                }
            },
        contentAlignment = Alignment.CenterStart
    ){
        Row(modifier = Modifier.padding(10.dp)){
            Icon(icon, contentDescription = "Localized description",
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .background(
                        color = color,
                        RoundedCornerShape(20)
                    ), tint = Color.White
            )
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = name, fontSize = 17.sp
            )
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Localized description",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .background(
                        color = Color.White,
                        RoundedCornerShape(20)
                    ), tint = Color.Gray
            )
        }
    }
}




