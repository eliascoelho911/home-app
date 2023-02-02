package com.eliascoelho911.homeapp.robotcar.data.service

import android.Manifest
import androidx.annotation.RequiresPermission
import com.eliascoelho911.homeapp.commons.service.BluetoothClientService
import com.eliascoelho911.homeapp.robotcar.domain.model.Movement
import com.eliascoelho911.homeapp.robotcar.domain.model.Robot
import com.eliascoelho911.homeapp.robotcar.infrastructure.BluetoothDeviceManager

internal class RobotBluetoothService(
    private val bluetoothClientService: BluetoothClientService,
    private val bluetoothDeviceManager: BluetoothDeviceManager
) {

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    suspend fun connect() {
        bluetoothDeviceManager.getDeviceOrNull()?.let {
            bluetoothClientService.connect(it)
        } ?: TODO("ERRO")
    }

    suspend fun updateWheels(robot: Robot) {
        with(bluetoothClientService) {
            write(robot.movement.toByteArray())
            flush()
        }
    }
}

private fun Movement.toByteArray() =
    "$value".encodeToByteArray()