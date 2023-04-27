package com.example.homerosense.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homerosense.data.ConnectionState
import com.example.homerosense.data.TemperatureAndHumidityReceiveManager
import com.example.homerosense.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TempHumidityViewModel @Inject constructor(
    private val temperatureAndHumidityReceiveManager: TemperatureAndHumidityReceiveManager
) : ViewModel(){

    var initializingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var temperature by mutableStateOf(0f)
        private set

    var humidity by mutableStateOf(0f)
        private set

    var temperaturehumidityDate by mutableStateOf<MutableList<String>>(arrayListOf())
        private set

    var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    private fun subscribeToChanges(){
        viewModelScope.launch {
            temperatureAndHumidityReceiveManager.data.collect{ result ->
                when(result){
                    is Resource.Success -> {
                        connectionState = result.data.connectionState
                        temperature = result.data.temperature
                        humidity = result.data.humidity
                        temperaturehumidityDate = result.data.temphum
                    }

                    is Resource.Loading -> {
                        initializingMessage = result.message
                        connectionState = ConnectionState.CurrentlyInitializing
                    }

                    is Resource.Error -> {
                        errorMessage = result.errorMessage
                        connectionState = ConnectionState.Uninitialized
                    }
                }
            }
        }
    }

    fun makeRequest(request: String){
        temperatureAndHumidityReceiveManager.writeCharacteristic(request)
    }

    fun closeConnection(){
        temperatureAndHumidityReceiveManager.closeConnection()
    }

    fun disconnect(){
        temperatureAndHumidityReceiveManager.disconnect()
    }

    fun reconnect(){
        temperatureAndHumidityReceiveManager.reconnect()
    }

    fun initializeConnection(){
        errorMessage = null
        subscribeToChanges()
        temperatureAndHumidityReceiveManager.startReceiving()
    }

    override fun onCleared() {
        super.onCleared()
        temperatureAndHumidityReceiveManager.closeConnection()
    }


}


