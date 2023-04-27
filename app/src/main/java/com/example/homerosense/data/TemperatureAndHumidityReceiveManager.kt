package com.example.homerosense.data

import com.example.homerosense.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface TemperatureAndHumidityReceiveManager {

    val data: MutableSharedFlow<Resource<TempHumidityResult>>

    fun writeCharacteristic(request: String)

    fun reconnect()

    fun disconnect()

    fun startReceiving()

    fun closeConnection()

}