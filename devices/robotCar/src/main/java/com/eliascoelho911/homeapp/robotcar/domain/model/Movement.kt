package com.eliascoelho911.homeapp.robotcar.domain.model

internal enum class Movement(val value: Int) {
    STOPPED(0),
    FORWARD_LEFT(1), FORWARD_RIGHT(2), FORWARD(3),
    BACKWARD_LEFT(4), BACKWARD_RIGHT(5), BACKWARD(6);
}