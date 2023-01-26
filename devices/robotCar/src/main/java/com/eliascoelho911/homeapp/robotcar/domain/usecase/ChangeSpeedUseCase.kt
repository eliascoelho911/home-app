package com.eliascoelho911.homeapp.robotcar.domain.usecase

import android.bluetooth.BluetoothSocket
import com.eliascoelho911.homeapp.robotcar.domain.model.Speed
import com.eliascoelho911.homeapp.robotcar.domain.model.Speed2
import com.eliascoelho911.homeapp.robotcar.domain.repository.RobotCarRepository

internal class ChangeSpeedUseCase(
    private val repository: RobotCarRepository
) {
    suspend fun updateSpeed(bluetoothSocket: BluetoothSocket, speed: Speed2) {
        repository.updateSpeed(bluetoothSocket, speed)
    }

    suspend fun updateLeftWheelSpeed(bluetoothSocket: BluetoothSocket, speed: Speed) {
        repository.updateLeftWheelSpeed(bluetoothSocket, speed)
    }

    suspend fun updateRightWheelVelocity(bluetoothSocket: BluetoothSocket, speed: Speed) {
        repository.updateRightWheelVelocity(bluetoothSocket, speed)
    }
}