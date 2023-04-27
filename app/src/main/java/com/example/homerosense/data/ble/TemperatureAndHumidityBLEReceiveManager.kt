package com.example.homerosense.data.ble

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.homerosense.data.ConnectionState
import com.example.homerosense.data.TempHumidityResult
import com.example.homerosense.data.TemperatureAndHumidityReceiveManager
import com.example.homerosense.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@SuppressLint("MissingPermission")
class TemperatureAndHumidityBLEReceiveManager @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context
) : TemperatureAndHumidityReceiveManager {

    private val DEVICE_NAME = "Nano33BLE"
    private val TEMP_HUMIDITY_SERVICE_UIID = "19b10000-e8f2-537e-4f6c-d104768a1214"
    private val TEMP_HUMIDITY_CHARACTERISTICS_UUID = "19b10001-e8f2-537e-4f6c-d104768a1214"

    override val data: MutableSharedFlow<Resource<TempHumidityResult>> = MutableSharedFlow()
    private val saveData : MutableList<String> = arrayListOf()

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private var gatt: BluetoothGatt? = null

    private var isScanning = false

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val scanCallback = object : ScanCallback(){

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if(result.device.name == DEVICE_NAME){
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Connecting to device..."))
                }
                if(isScanning){
                    result.device.connectGatt(context,false, gattCallback)
                    isScanning = false
                    bleScanner.stopScan(this)
                }
            }
        }
    }

    private var currentConnectionAttempt = 1
    private var MAXIMUM_CONNECTION_ATTEMPTS = 5

    private val gattCallback = object : BluetoothGattCallback(){
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                if(newState == BluetoothProfile.STATE_CONNECTED){
                    coroutineScope.launch {
                        data.emit(Resource.Loading(message = "Discovering Services..."))
                    }
                    gatt.discoverServices()
                    this@TemperatureAndHumidityBLEReceiveManager.gatt = gatt
                } else if(newState == BluetoothProfile.STATE_DISCONNECTED){
//                    coroutineScope.launch {
//                        data.emit(Resource.Success(data = TempHumidityResult(0f,0f,ConnectionState.Disconnected)))
//                    }
                    val tempHumidityResult = TempHumidityResult(
                        0F,0F,
                        saveData,
                        ConnectionState.Disconnected
                    )
                    coroutineScope.launch {
                        data.emit(
                            Resource.Success(data = tempHumidityResult)
                        )
                    }
                    gatt.close()
                }
            }else{
                gatt.close()
                currentConnectionAttempt+=1
                coroutineScope.launch {
                    data.emit(
                        Resource.Loading(
                            message = "Attempting to connect $currentConnectionAttempt/$MAXIMUM_CONNECTION_ATTEMPTS"
                        )
                    )
                }
                if(currentConnectionAttempt<=MAXIMUM_CONNECTION_ATTEMPTS){
                    startReceiving()
                }else{
                    coroutineScope.launch {
                        data.emit(Resource.Error(errorMessage = "Could not connect to ble device"))
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            with(gatt){
                printGattTable()
                val characteristic = findCharacteristics(TEMP_HUMIDITY_SERVICE_UIID, TEMP_HUMIDITY_CHARACTERISTICS_UUID)
                if(characteristic == null){
                    coroutineScope.launch {
                        data.emit(Resource.Error(errorMessage = "Could not find temp and humidity publisher"))
                    }
                    return
                }
                enableNotification(characteristic)

            }

        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            with(characteristic){
                when(uuid){
                    UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID) -> {
                        //XX XX XX XX XX XX

//                        val debugLogcat = mutableListOf("");
//                        debugLogcat.removeFirst()
//                        for (i in value.indices) {
//                            debugLogcat.add(value[i].toString())
//                        }
//                        Log.d("Debug value ", debugLogcat.toString())

                        //val temperature = value[0].toInt().toChar() // Decode char

                        if (value[0].toInt() == 1) {
                            val temperature = (value[1].toInt().toString() + "." + value[2].toInt()
                                .toString()).toFloat()
                            val humidity = (value[3].toInt().toString() + "." + value[4].toInt()
                                .toString()).toFloat()
                            Log.d("Output", "Temperature: $temperature- Humidity: $humidity")


                            /* Time for characteristic change*/
                            val current = LocalDateTime.now()
                            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                            val tokyoJupiter = current.format(formatter)
                            val characteristicChange = ("Time: $tokyoJupiter | T: $temperature | " +
                                    "H: $humidity ")

                            saveData.add(characteristicChange)


                            val tempHumidityResult = TempHumidityResult(
                                temperature, humidity,
                                saveData,
                                ConnectionState.Connected
                            )

                            coroutineScope.launch {
                                data.emit(
                                    Resource.Success(data = tempHumidityResult)
                                )
                            }

                        }


                    }
                    else -> Unit
                }
            }
        }

    }


    override fun writeCharacteristic(request: String) {
        val characteristic = findCharacteristics(TEMP_HUMIDITY_SERVICE_UIID, TEMP_HUMIDITY_CHARACTERISTICS_UUID)
        val payload = request.toByteArray()
        val writeType = when {
            characteristic?.isWritable() == true -> BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            else -> error("Characteristic ${characteristic?.uuid} cannot be written to")
        }

        gatt?.let { gatt ->
            characteristic.writeType = writeType
            characteristic.value = payload
            gatt.writeCharacteristic(characteristic)
        } ?: error("Not connected to a BLE device!")
    }

    private fun enableNotification(characteristic: BluetoothGattCharacteristic){
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        val payload = when {
            characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            else -> return
        }

        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if(gatt?.setCharacteristicNotification(characteristic, true) == false){
                Log.d("BLEReceiveManager","set characteristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, payload)
        }
    }

    private fun writeDescription(descriptor: BluetoothGattDescriptor, payload: ByteArray){
        gatt?.let { gatt ->
            descriptor.value = payload
            gatt.writeDescriptor(descriptor)
        } ?: error("Not connected to a BLE device!")
    }

    private fun findCharacteristics(serviceUUID: String, characteristicsUUID:String):BluetoothGattCharacteristic?{
        return gatt?.services?.find { service ->
            service.uuid.toString() == serviceUUID
        }?.characteristics?.find { characteristics ->
            characteristics.uuid.toString() == characteristicsUUID
        }
    }

    override fun startReceiving() {
        coroutineScope.launch {
            data.emit(Resource.Loading(message = "Scanning Ble devices..."))
        }
        isScanning = true
        bleScanner.startScan(null,scanSettings,scanCallback)
    }

    override fun reconnect() {
        gatt?.connect()
    }

    override fun disconnect() {
        Log.d("BluetoothGatt:", "disconnect()")
        gatt?.disconnect()
    }

    override fun closeConnection() {
        bleScanner.stopScan(scanCallback)
        val characteristic = findCharacteristics(TEMP_HUMIDITY_SERVICE_UIID, TEMP_HUMIDITY_CHARACTERISTICS_UUID)
        if(characteristic != null){
            disconnectCharacteristic(characteristic)
        }
        Log.d("BluetoothGatt:", "closeConnection()")
        gatt?.close()
    }

    private fun disconnectCharacteristic(characteristic: BluetoothGattCharacteristic){
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if(gatt?.setCharacteristicNotification(characteristic,false) == false){
                Log.d("TempHumidReceiveManager","set charateristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
        }
    }

}


