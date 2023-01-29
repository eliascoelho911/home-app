package com.eliascoelho911.homeapp.robotcar.data.repository

import android.Manifest
import androidx.annotation.RequiresPermission
import com.eliascoelho911.homeapp.robotcar.data.service.BluetoothClientService
import com.eliascoelho911.homeapp.robotcar.domain.model.Speed
import com.eliascoelho911.homeapp.robotcar.domain.repository.RobotCarRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

private const val RETRY_DELAY = 1000L

private const val TIMEOUT = 1000L

private const val ATTEMPTS = 3

internal class RobotCarBluetoothRepositoryImpl(
    private val bluetoothService: BluetoothClientService
) : RobotCarRepository {

    private var attemptsCount = 0

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override suspend fun connect() {
        withTimeout(TIMEOUT) {
            runCatching {
                bluetoothService.connect()
            }.onFailure {
                connectRetry(it)
            }.onSuccess {
                return@withTimeout
            }
        }
        connectRetry()
    }

    override suspend fun send(leftWheel: Speed?, rightWheel: Speed?) {
        bluetoothService.sendSpeed(leftWheel = leftWheel, rightWheel = rightWheel)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private suspend fun connectRetry(throwable: Throwable? = null) {
        if (attemptsCount < ATTEMPTS) {
            delay(RETRY_DELAY)
            attemptsCount++
            connect()
        } else {
            throwable?.let { throw it }
        }
    }
}