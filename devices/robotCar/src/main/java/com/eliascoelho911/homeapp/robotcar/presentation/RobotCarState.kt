package com.eliascoelho911.homeapp.robotcar.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

data class RobotCarState(
    val information: Information = Information(),
    val control: Control
) {
    data class Information(
        val name: String = "",
        val connection: Connection? = null
    )

    data class Connection(
        val color: Color,
        val icon: Painter,
        val description: String
    )

    data class Control(
        val leftValue: Float,
        val rightValue: Float
    )
}
