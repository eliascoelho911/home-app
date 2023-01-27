package com.eliascoelho911.homeapp.robotcar.presentation

import androidx.annotation.StringRes
import com.eliascoelho911.homeapp.robotcar.R

internal val initialState = RobotCarState(
    information = RobotCarState.Information(
        nameRes = R.string.device_name,
        connectionDescriptionRes = R.string.connected_label
    ),
    control = RobotCarState.Control(
        leftValue = 0.5f, rightValue = 0.5f
    )
)

data class RobotCarState(
    val information: Information,
    val control: Control
) {
    data class Information(
        @StringRes val nameRes: Int,
        @StringRes val connectionDescriptionRes: Int? = null
    )

    data class Control(
        val leftValue: Float,
        val rightValue: Float
    )
}
