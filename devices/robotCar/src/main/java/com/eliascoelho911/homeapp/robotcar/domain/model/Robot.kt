package com.eliascoelho911.homeapp.robotcar.domain.model

internal data class Robot(val movement: Movement) {

    fun forward(): Robot {
        return copy(movement = Movement.FORWARD)
    }

    fun backward(): Robot {
        return copy(movement = Movement.BACKWARD)
    }

    fun left(): Robot {
        return copy(
            movement = if (isForward()) Movement.FORWARD_LEFT else Movement.BACKWARD_LEFT
        )
    }

    fun right(): Robot {
        return copy(
            movement = if (isForward()) Movement.FORWARD_RIGHT else Movement.BACKWARD_RIGHT
        )
    }

    fun stop(): Robot {
        return copy(movement = Movement.STOPPED)
    }
}

private fun Robot.isForward(): Boolean =
    movement == Movement.FORWARD ||
            movement == Movement.FORWARD_RIGHT ||
            movement == Movement.FORWARD_LEFT