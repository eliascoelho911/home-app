package com.eliascoelho911.homeapp.robotcar.domain.usecase

import com.eliascoelho911.homeapp.robotcar.domain.model.Robot
import com.eliascoelho911.homeapp.robotcar.domain.repository.RobotRepository

internal class UpdateRobotUseCase(
    private val repository: RobotRepository
) {
    suspend operator fun invoke(robot: Robot.() -> Robot) {
        repository.update(robot(repository.get()))
    }
}