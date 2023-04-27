package com.example.homerosense.presentation

import android.bluetooth.BluetoothAdapter
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.homerosense.data.ConnectionState
import com.example.homerosense.presentation.permissions.PermissionUtils
import com.example.homerosense.presentation.permissions.SystemBroadcastReceiver
import com.example.homerosense.presentation.screen.Screen
import com.example.homerosense.ui.theme.AppleBlue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothLoadingScreen(
    onBluetoothStateChanged: () -> Unit,
    navController: NavController,
    viewModel: TempHumidityViewModel
) {

    SystemBroadcastReceiver(systemAction = BluetoothAdapter.ACTION_STATE_CHANGED){ bluetoothState ->
        val action = bluetoothState?.action ?: return@SystemBroadcastReceiver
        if(action == BluetoothAdapter.ACTION_STATE_CHANGED){
            onBluetoothStateChanged()
        }
    }

    val permissionState = rememberMultiplePermissionsState(permissions = PermissionUtils.permissions)
    val lifecycleOwner = LocalLifecycleOwner.current
    val bleConnectionState = viewModel.connectionState


//    DisposableEffect(
//        key1 = lifecycleOwner,
//        effect = {
//            val observer = LifecycleEventObserver{_,event ->
//                if(event == Lifecycle.Event.ON_START){
//                    permissionState.launchMultiplePermissionRequest()
//                    if(permissionState.allPermissionsGranted && bleConnectionState == ConnectionState.Disconnected){
//                        viewModel.reconnect()
//                    }
//                }
//                if(event == Lifecycle.Event.ON_STOP){
//                    if (bleConnectionState == ConnectionState.Connected){
//                        viewModel.disconnect()
//                    }
//                }
//            }
//            lifecycleOwner.lifecycle.addObserver(observer)
//
//            onDispose {
//                lifecycleOwner.lifecycle.removeObserver(observer)
//            }
//        }
//    )

    LaunchedEffect(key1 = permissionState.allPermissionsGranted){
        if(permissionState.allPermissionsGranted){
            if(bleConnectionState == ConnectionState.Uninitialized){
                viewModel.initializeConnection()
            }
        }
    }

    Button(onClick = {
        navController.navigate(Screen.NavigationTabs.route) {
            popUpTo(Screen.BluetoothLoadingScreen.route) {
                inclusive = true
            }
        }
    }) {

    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .border(
                    BorderStroke(
                        5.dp, AppleBlue
                    ),
                    RoundedCornerShape(10.dp)
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(bleConnectionState == ConnectionState.CurrentlyInitializing){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    CircularProgressIndicator(color = AppleBlue)
                    if(viewModel.initializingMessage != null){
                        Text(
                            text = viewModel.initializingMessage!!
                        )
                    }
                }
            }else if(!permissionState.allPermissionsGranted){
                Text(
                    text = "Go to the app setting and allow the missing permissions.",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Center
                )
            }else if(viewModel.errorMessage != null){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = viewModel.errorMessage!!
                    )
                    Button(
                        onClick = {
                            if(permissionState.allPermissionsGranted){
                                viewModel.initializeConnection()
                            }
                        }
                    ) {
                        Text(
                            "Try again"
                        )
                    }
                }
            }else if(bleConnectionState == ConnectionState.Connected){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){

                    Text(
                        "Success"
                    )

                    LaunchedEffect(key1 = ConnectionState.Connected){
                        navController.navigate(Screen.NavigationTabs.route) {
                            popUpTo(Screen.BluetoothLoadingScreen.route) {
                                inclusive = true
                            }
                        }
                    }

                }
            }else if(bleConnectionState == ConnectionState.Disconnected){
                Button(onClick = {
                    viewModel.initializeConnection()
                }) {
                    Text("Initialize again")
                }
            }


        }
    }


}