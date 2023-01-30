package com.eliascoelho911.homeapp.robotcar.data.service

import android.Manifest
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.annotation.RequiresPermission
import com.eliascoelho911.homeapp.robotcar.domain.model.Speed
import com.eliascoelho911.homeapp.robotcar.infrastructure.BluetoothDeviceManager
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val INITIAL_SPEED = 0

class BluetoothClientService(
    private val deviceManager: BluetoothDeviceManager,
    private val uuid: UUID
) : OutputService {

    private var socket: BluetoothSocket? = null

    private val isConnected: Boolean get() = socket != null

    private var currentLeftWheelSpeed: Speed = INITIAL_SPEED

    private var currentRightWheelSpeed: Speed = INITIAL_SPEED

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override suspend fun connect() {
        withContext(Dispatchers.IO) {
            if (!isConnected) {
                deviceManager.getDeviceOrNull()?.let { device ->
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
    }

    suspend fun sendSpeed(leftWheel: Speed?, rightWheel: Speed?) {
        val leftWheelFinal = leftWheel ?: currentLeftWheelSpeed
        val rightWheelFinal = rightWheel ?: currentRightWheelSpeed

        write(leftWheelFinal.toByteArray())
        currentLeftWheelSpeed = leftWheelFinal

        write(rightWheelFinal.toByteArray())
        currentRightWheelSpeed = rightWheelFinal

        flush()
    }

    override suspend fun write(byteArray: ByteArray) {
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
            }
        }
    }

    override suspend fun flush() {
        withContext(Dispatchers.IO) {
            socket?.outputStream?.flush()
            log("flush on ${socket?.remoteDevice?.address}")
        }
    }

    override fun close() {
        log("Closing connection with ${socket?.remoteDevice?.address}")
        socket?.close()
        socket = null
    }

    private fun log(msg: String) {
        Log.d("BluetoothClientService", msg)
    }
}

private fun Speed.toByteArray() = toString().encodeToByteArray()