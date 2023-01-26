@file:OptIn(ExperimentalMaterial3Api::class)

package com.eliascoelho911.homeapp.robotcar.presentation

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eliascoelho911.homeapp.ds.components.VerticalControl
import com.eliascoelho911.homeapp.ds.theme.HomeAppTheme
import com.eliascoelho911.homeapp.robotcar.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel
import com.eliascoelho911.homeapp.ds.R as DS

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RobotCarScreen() {
    val viewModel: RobotCarViewModel = koinViewModel()
    val state by viewModel.state.observeAsState(initial = initialState())
    val context = LocalContext.current
    val btConnectPermissionState = rememberPermissionState(permission = BLUETOOTH_CONNECT)

    ActionHandler(viewModel, btConnectPermissionState)

    RobotCarScreenContent(
        state = state,
        onClickNavigationIcon = {},
        onValueLeftChanged = {
            if (btConnectPermissionState.status.isGranted) {
                viewModel.onLeftWheelVelocityChanged(context, it)
            } else {
                viewModel.onRequestPermission(BLUETOOTH_CONNECT)
            }
        },
        onValueRightChanged = {
            if (btConnectPermissionState.status.isGranted) {
                viewModel.onRightWheelVelocityChanged(context, it)
            } else {
                viewModel.onRequestPermission(BLUETOOTH_CONNECT)
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ActionHandler(
    viewModel: RobotCarViewModel,
    btConnectPermissionState: PermissionState
) {
    val owner = LocalLifecycleOwner.current

    SideEffect {
        viewModel.actions.observe(owner) {
            when (it) {
                is RobotCarAction.RequestPermission -> {
                    btConnectPermissionState.launchPermissionRequest()
                }
            }
        }
    }
}

@Composable
private fun initialState() = RobotCarState(
    control = RobotCarState.Control(
        leftValue = 0.5f, rightValue = 0.5f
    )
)

@Composable
private fun RobotCarScreenContent(
    state: RobotCarState,
    onClickNavigationIcon: () -> Unit,
    onValueLeftChanged: (Float) -> Unit,
    onValueRightChanged: (Float) -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { RobotCarTopBar(onClickNavigationIcon) }
    ) { internalPadding ->
        Column(
            modifier = Modifier
                .padding(internalPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Information(information = state.information)
            Control(
                state = state,
                onValueLeftChanged = onValueLeftChanged,
                onValueRightChanged = onValueRightChanged
            )
        }
    }
}

@Composable
private fun RobotCarTopBar(onClickNavigationIcon: () -> Unit) {
    TopAppBar(navigationIcon = {
        IconButton(onClick = onClickNavigationIcon) {
            Icon(
                painter = painterResource(id = DS.drawable.ic_back),
                contentDescription = null
            )
        }
    }, title = {})
}

@Composable
private fun Information(
    information: RobotCarState.Information
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = information.name,
            style = MaterialTheme.typography.displayMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        information.connection?.let { connection ->
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = connection.icon,
                    contentDescription = null,
                    tint = connection.color
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = connection.description,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    color = connection.color
                )
            }
        }
    }
}

@Composable
private fun Control(
    state: RobotCarState,
    onValueLeftChanged: (Float) -> Unit,
    onValueRightChanged: (Float) -> Unit
) {
    VerticalControl(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 56.dp, end = 56.dp, bottom = 96.dp)
            .height(300.dp),
        valueLeft = state.control.leftValue,
        valueRight = state.control.rightValue,
        onValueLeftChanged = onValueLeftChanged,
        onValueRightChanged = onValueRightChanged
    )
}

private const val darkThemePreview = false

@Composable
@Preview(device = "id:pixel_2")
private fun ScreenPreview() {
    HomeAppTheme(useDarkTheme = darkThemePreview) {
        var valueLeft by remember { mutableStateOf(0.5f) }
        var valueRight by remember { mutableStateOf(0.5f) }

        RobotCarScreenContent(
            state = RobotCarState(
                information = RobotCarState.Information(
                    name = "Carrinho robô",
                    connection = RobotCarState.Connection(
                        color = MaterialTheme.colorScheme.secondary,
                        icon = painterResource(id = DS.drawable.ic_connection),
                        description = stringResource(id = R.string.connected_label)
                    )
                ),
                control = RobotCarState.Control(
                    leftValue = valueLeft,
                    rightValue = valueRight
                )
            ),
            onClickNavigationIcon = {},
            onValueLeftChanged = { valueLeft = it },
            onValueRightChanged = { valueRight = it }
        )
    }
}