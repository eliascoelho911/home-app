package com.eliascoelho911.homeapp.robotcar.domain.usecase

import com.eliascoelho911.homeapp.robotcar.domain.model.Speed
import com.eliascoelho911.homeapp.robotcar.domain.repository.RobotCarRepository

private const val FORWARD = 1

private const val BACKWARD = -1

private const val ZERO = 0

internal class UpdateSpeedUseCase(
    private val repository: RobotCarRepository
) {
    suspend fun forward() {
        sendSpeed(leftWheel = FORWARD, rightWheel = FORWARD)
    }

    suspend fun left() {
        sendSpeed(leftWheel = ZERO)
    }

    suspend fun right() {
        sendSpeed(rightWheel = ZERO)
    }

    suspend fun backward() {
        sendSpeed(leftWheel = BACKWARD, rightWheel = BACKWARD)
    }

    suspend fun stop() {
        sendSpeed(leftWheel = ZERO, rightWheel = ZERO)
    }

    private suspend fun sendSpeed(leftWheel: Speed? = null, rightWheel: Speed? = null) {
        repository.send(leftWheel, rightWheel)
    }
}