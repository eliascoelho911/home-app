package com.eliascoelho911.homeapp.robotcar.presentation

internal sealed interface RobotCarAction {
    class RequestPermission(permission: String) : RobotCarAction
}