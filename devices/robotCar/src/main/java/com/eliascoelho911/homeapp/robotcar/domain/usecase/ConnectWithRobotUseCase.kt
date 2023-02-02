package com.eliascoelho911.homeapp.robotcar.domain.usecase

import com.eliascoelho911.homeapp.robotcar.domain.repository.RobotRepository

internal class ConnectWithRobotUseCase(
    private val repository: RobotRepository
) {
    suspend operator fun invoke() {
        repository.connect()
    }
}