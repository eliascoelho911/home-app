package com.eliascoelho911.homeapp.robotcar.presentation

internal sealed interface RobotAction {
    class RequestPermission(permission: String) : RobotAction
}