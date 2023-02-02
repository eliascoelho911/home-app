package com.eliascoelho911.homeapp.robotcar.domain.repository

import com.eliascoelho911.homeapp.robotcar.domain.model.Robot

internal interface RobotRepository {

    suspend fun connect()

    suspend fun update(robot: Robot)

    suspend fun get(): Robot
}