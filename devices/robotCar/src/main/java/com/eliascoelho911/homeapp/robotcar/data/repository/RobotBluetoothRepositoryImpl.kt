package com.eliascoelho911.homeapp.robotcar.data.repository

import android.Manifest
import androidx.annotation.RequiresPermission
import com.eliascoelho911.homeapp.robotcar.data.cache.RobotLocalStorage
import com.eliascoelho911.homeapp.robotcar.data.service.RobotBluetoothService
import com.eliascoelho911.homeapp.robotcar.domain.model.Robot
import com.eliascoelho911.homeapp.robotcar.domain.repository.RobotRepository
import kotlinx.coroutines.delay

private const val RETRY_DELAY = 1000L

private const val ATTEMPTS = 3

internal class RobotBluetoothRepositoryImpl(
    private val robotBluetoothService: RobotBluetoothService,
    private val robotLocalStorage: RobotLocalStorage
) : RobotRepository {

    private var attemptsCount = 0

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override suspend fun connect() {
        runCatching {
            robotBluetoothService.connect()
        }.onFailure {
            connectRetry(it)
        }.onSuccess { return }
    }

    override suspend fun update(robot: Robot) {
        robotBluetoothService.updateWheels(robot)
        robotLocalStorage.set(robot)
    }

    override suspend fun get(): Robot {
        return robotLocalStorage.get()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private suspend fun connectRetry(throwable: Throwable? = null) {
        if (attemptsCount < ATTEMPTS) {
            delay(RETRY_DELAY)
            attemptsCount++
            connect()
        } else throwable?.let { throw it }
    }
}