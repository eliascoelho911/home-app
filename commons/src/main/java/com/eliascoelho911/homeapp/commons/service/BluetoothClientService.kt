package com.eliascoelho911.homeapp.commons.service

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.annotation.RequiresPermission
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BluetoothClientService(private val uuid: UUID) {

    private var socket: BluetoothSocket? = null

    private val isConnected: Boolean get() = socket != null

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    suspend fun connect(device: BluetoothDevice) {
        withContext(Dispatchers.IO) {
            if (!isConnected) {
                val tmpSocket = device.createRfcommSocketToServiceRecord(uuid)
                socket = tmpSocket

                runCatching {
                    tmpSocket.connect()
                }.onSuccess {
                    socket = tmpSocket
                    log("Connected to device ${device.name}")
                }.onFailure {
                    log("Error on try connect to device ${device.name} - ${device.address}")
                    close()
                    throw it
                }
            }
        }
    }

    suspend fun write(byteArray: ByteArray) {
        withContext(Dispatchers.IO) {
            if (isConnected) {
                runCatching {
                    socket?.outputStream?.write(byteArray)
                }.onSuccess {
                    log("Writing ${byteArray.decodeToString()} to ${socket?.remoteDevice?.address}")
                }.onFailure {
                    log("Error on try write ${byteArray.decodeToString()} to ${socket?.remoteDevice?.address}")
                    close()
                    throw it
                }
            } else TODO("ERROR")
        }
    }

    suspend fun flush() {
        withContext(Dispatchers.IO) {
            socket?.outputStream?.flush()
            log("flush on ${socket?.remoteDevice?.address}")
        }
    }

    fun close() {
        log("Closing connection with ${socket?.remoteDevice?.address}")
        socket?.close()
        socket = null
    }

    private fun log(msg: String) {
        Log.d("BluetoothClientService", msg)
    }
}