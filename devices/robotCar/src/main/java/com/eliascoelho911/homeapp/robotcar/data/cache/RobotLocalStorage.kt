package com.eliascoelho911.homeapp.robotcar.data.cache

import com.eliascoelho911.homeapp.robotcar.domain.model.Movement
import com.eliascoelho911.homeapp.robotcar.domain.model.Robot

private val INITIAL = Robot(movement = Movement.STOPPED)

internal class RobotLocalStorage {
    private var currentRobot: Robot = INITIAL

    fun get(): Robot = currentRobot

    fun set(robot: Robot) {
        this.currentRobot = robot
    }
}