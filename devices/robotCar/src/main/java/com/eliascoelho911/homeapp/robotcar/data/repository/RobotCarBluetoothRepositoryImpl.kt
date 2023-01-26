package com.eliascoelho911.homeapp.robotcar.data.repository

import android.Manifest
import android.bluetooth.BluetoothSocket
import androidx.annotation.RequiresPermission
import com.eliascoelho911.homeapp.robotcar.domain.model.Speed
import com.eliascoelho911.homeapp.robotcar.domain.model.Speed2
import com.eliascoelho911.homeapp.robotcar.domain.repository.RobotCarRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class RobotCarBluetoothRepositoryImpl(
    private val dispatcher: CoroutineDispatcher
) : RobotCarRepository {

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override suspend fun updateSpeed(bluetoothSocket: BluetoothSocket, speed: Speed2) {
        send(bluetoothSocket, bytes = speed.toSpeedInt())
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override suspend fun updateLeftWheelSpeed(bluetoothSocket: BluetoothSocket, speed: Speed) {
        send(bluetoothSocket, speed)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override suspend fun updateRightWheelVelocity(bluetoothSocket: BluetoothSocket, speed: Speed) {
        send(bluetoothSocket, speed)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private suspend fun send(socket: BluetoothSocket, speed: Speed) {
        send(socket, bytes = speed.toSpeedInt())
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private suspend fun send(socket: BluetoothSocket, bytes: Int) {
        withContext(dispatcher) {
            runCatching {
                socket.connect()
                socket.outputStream
            }.onSuccess { outStream ->
                outStream.write(bytes)
            }
        }
    }
}

private fun Speed2.toSpeedInt(): Int {
    return "${leftWheel.toSpeedInt()}${rightWheel.toSpeedInt()}".toInt()
}

private fun Speed.toSpeedInt(): Int {
    return when {
        this < 0.5f -> 1
        this == 0.5f -> 2
        else -> 3
    }
}