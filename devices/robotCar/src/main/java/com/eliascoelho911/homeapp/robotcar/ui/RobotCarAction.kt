package com.eliascoelho911.homeapp.robotcar.ui

internal sealed interface RobotCarAction {
    class RequestPermission(permission: String) : RobotCarAction
}