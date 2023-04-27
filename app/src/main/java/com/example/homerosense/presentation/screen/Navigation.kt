package com.example.homerosense.presentation.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homerosense.presentation.*
import com.example.homerosense.presentation.screen.monitor.*
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Navigation(
    onBluetoothStateChanged:()->Unit,
    viewModel: TempHumidityViewModel = hiltViewModel()
) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.StartScreen.route){
        /* Main process */
        composable(Screen.StartScreen.route){
            StartScreen(navController = navController)
        }
        composable(Screen.BluetoothLoadingScreen.route){
            BluetoothLoadingScreen(navController = navController,
                onBluetoothStateChanged = onBluetoothStateChanged,
                viewModel = viewModel)
        }
        composable(Screen.NavigationTabs.route){
            NavigationTabs(
                onBluetoothStateChanged,
                navController = navController,
                viewModel = viewModel
            )
        }

        /* Monitor */
        composable(Screen.HistoryScreen.route){
           HistoryScreen(viewModel = viewModel)
        }
        composable(Screen.StatisticsScreen.route){
            StatisticsScreen(viewModel = viewModel)
        }
        composable(Screen.Ideas.route){
            TabLayout()
        }
        composable(Screen.Ideas2.route){
            Ideas2(viewModel = viewModel)
        }


    }

}

sealed class Screen(val route:String){
    /* Start screen*/
    object StartScreen:Screen("start_screen")
    object BluetoothLoadingScreen:Screen("bluetooth_loading_screen")

    /* Navigation Tabs -> Includes: Main, Monitor, Settings*/
    object NavigationTabs:Screen("tab_screen")
        /* Main -> Includes: null */
        object TemperatureHumidityScreen:Screen("temp_humid_screen")
        /* Monitor -> Includes: History, Statistics */
        object Monitor:Screen("monitor_screen")
            object HistoryScreen:Screen("monitor_history_screen")
            object StatisticsScreen:Screen("statistics_screen")
            object Ideas:Screen("ideas_screen")
            object Ideas2:Screen("ideas2_screen")
        /* Settings -> Includes: null */
        object Settings:Screen("settings_screen")




}