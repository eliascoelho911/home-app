package com.eliascoelho911.homeapp.robotcar.presentation

import androidx.annotation.StringRes
import com.eliascoelho911.homeapp.robotcar.R

data class RobotCarState(
    val information: Information
) {
    data class Information(
        @StringRes val nameRes: Int,
        @StringRes val connectionDescriptionRes: Int,
        val showRefresh: Boolean = false
    )
}

internal val initialState = RobotCarState(
    information = RobotCarState.Information(
        nameRes = R.string.device_name,
        connectionDescriptionRes = R.string.connecting_label
    )
)

internal val RobotCarState.connectingState
    get() = copy(
        information = information.copy(
            showRefresh = false,
            connectionDescriptionRes = R.string.connecting_label
        )
    )

internal val RobotCarState.disconnectedState
    get() = copy(
        information = information.copy(
            connectionDescriptionRes = R.string.disconnected_label,
            showRefresh = true
        )
    )

internal val RobotCarState.connectedState
    get() = copy(
        information = information.copy(
            connectionDescriptionRes = R.string.connected_label,
            showRefresh = false
        )
    )