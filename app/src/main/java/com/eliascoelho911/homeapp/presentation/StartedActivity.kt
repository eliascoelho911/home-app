package com.eliascoelho911.homeapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.eliascoelho911.homeapp.robotcar.presentation.RobotCarScreen

internal class StartedActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RobotCarScreen()
        }
    }
}