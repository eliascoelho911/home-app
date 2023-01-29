package com.eliascoelho911.homeapp.robotcar.domain.repository

import com.eliascoelho911.homeapp.robotcar.domain.model.Speed

internal interface RobotCarRepository {

    suspend fun connect()

    suspend fun send(leftWheel: Speed? = null, rightWheel: Speed? = null)
}