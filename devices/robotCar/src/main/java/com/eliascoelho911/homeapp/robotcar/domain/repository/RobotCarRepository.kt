package com.eliascoelho911.homeapp.robotcar.domain.repository

import android.bluetooth.BluetoothSocket
import com.eliascoelho911.homeapp.robotcar.domain.model.Speed
import com.eliascoelho911.homeapp.robotcar.domain.model.Speed2

internal interface RobotCarRepository {
    suspend fun updateSpeed(bluetoothSocket: BluetoothSocket, speed: Speed2)

    suspend fun updateLeftWheelSpeed(bluetoothSocket: BluetoothSocket, speed: Speed)

    suspend fun updateRightWheelVelocity(bluetoothSocket: BluetoothSocket, speed: Speed)
}