package com.example.homerosense.presentation.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.homerosense.data.ConnectionState
import com.example.homerosense.presentation.TempHumidityViewModel
import com.example.homerosense.presentation.screen.home.TemperatureHumidityScreen
import com.example.homerosense.presentation.screen.monitor.MonitorScreen
import com.example.homerosense.presentation.screen.settings.SettingsScreen
import com.example.homerosense.ui.theme.AppleRed
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun NavigationTabs(
    onBluetoothStateChanged: () -> Unit,
    navController: NavController,
    viewModel: TempHumidityViewModel
) {

    // on below line we are creating variable for pager state.
    val pagerState = rememberPagerState(0)
    Column(
        modifier = Modifier.background(Color.White)
    ) {

        Tabs(pagerState = pagerState)
        TabsContent(
            pagerState = pagerState,
            onBluetoothStateChanged = onBluetoothStateChanged,
            navController = navController,
            viewModel = viewModel
        )

    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        "Home" to Icons.Default.Home,
        "Monitor" to Icons.Default.DateRange,
        "Settings" to Icons.Default.Settings
    )
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = Color.Black,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = Color.Black
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                icon = {
                    Icon(imageVector = list[index].second, contentDescription = null)
                },
                text = {
                    Text(
                        list[index].first,
                        color = if (pagerState.currentPage == index) Color.Black else Color.Black
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }


}

@ExperimentalPagerApi
@Composable
fun TabsContent(
    pagerState: PagerState,
    onBluetoothStateChanged: () -> Unit,
    navController: NavController,
    viewModel: TempHumidityViewModel
){

    HorizontalPager(state = pagerState, count = 3) {
            page ->
        when (page) {
            0 -> TemperatureHumidityScreen(
                onBluetoothStateChanged = onBluetoothStateChanged, viewModel = viewModel)
            1 -> MonitorScreen(navController = navController, viewModel = viewModel)
            2 -> SettingsScreen(navController = navController)
        }
    }
}



