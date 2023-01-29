package com.eliascoelho911.homeapp.robotcar.ui

import androidx.annotation.StringRes
import com.eliascoelho911.homeapp.robotcar.R

internal val initialState = RobotCarState(
    information = RobotCarState.Information(
        nameRes = R.string.device_name,
        connectionDescriptionRes = R.string.connecting_label
    )
)

data class RobotCarState(
    val information: Information
) {
    data class Information(
        @StringRes val nameRes: Int,
        @StringRes val connectionDescriptionRes: Int,
        val showRefresh: Boolean = false
    )
}
