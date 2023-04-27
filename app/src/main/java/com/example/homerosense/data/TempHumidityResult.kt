package com.example.homerosense.data

data class TempHumidityResult(
    val temperature:Float,
    val humidity:Float,
    val temphum: MutableList<String>,
    val connectionState: ConnectionState
)
