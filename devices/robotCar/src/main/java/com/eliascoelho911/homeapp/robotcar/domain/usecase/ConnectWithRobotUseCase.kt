package com.eliascoelho911.homeapp.robotcar.domain.usecase

import com.eliascoelho911.homeapp.robotcar.domain.repository.RobotCarRepository

internal class ConnectWithRobotUseCase(
    private val repository: RobotCarRepository
) {
    suspend operator fun invoke() {
        repository.connect()
    }
}