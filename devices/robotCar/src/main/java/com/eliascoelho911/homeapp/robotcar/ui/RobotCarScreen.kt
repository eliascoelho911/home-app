@file:OptIn(ExperimentalMaterial3Api::class)

package com.eliascoelho911.homeapp.robotcar.ui

import android.Manifest.permission.BLUETOOTH_CONNECT
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eliascoelho911.homeapp.ds.theme.HomeAppTheme
import com.eliascoelho911.homeapp.robotcar.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel
import com.eliascoelho911.homeapp.ds.R as DS

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RobotCarScreen() {
    val viewModel: RobotCarViewModel = koinViewModel()
    val state by viewModel.state.observeAsState(initial = initialState)
    val btConnectPermissionState = rememberPermissionState(permission = BLUETOOTH_CONNECT)

    ActionHandler(viewModel, btConnectPermissionState)

    RobotCarScreenContent(
        state = state,
        onClickNavigationIcon = {},
        onClickLeft = {
            viewModel.onClickLeft(btConnectPermissionState)
        },
        onClickRight = {
            viewModel.onClickRight(btConnectPermissionState)
        },
        onClickForward = {
            viewModel.onClickForward(btConnectPermissionState)
        },
        onClickBackward = {
            viewModel.onClickBackward(btConnectPermissionState)
        },
        onClickStop = {
            viewModel.onClickStop(btConnectPermissionState)
        },
        onClickRefresh = {
            viewModel.onClickRefresh()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RobotCarScreenContent(
    state: RobotCarState,
    onClickNavigationIcon: () -> Unit,
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit,
    onClickForward: () -> Unit,
    onClickBackward: () -> Unit,
    onClickStop: () -> Unit,
    onClickRefresh: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { RobotCarTopBar(onClickNavigationIcon) }
    ) { internalPadding ->
        Column(
            modifier = Modifier
                .padding(internalPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Information(information = state.information, onClickRefresh = onClickRefresh)
            Control(
                onClickLeft = onClickLeft,
                onClickRight = onClickRight,
                onClickForward = onClickForward,
                onClickBackward = onClickBackward,
                onClickStop = onClickStop
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    information: RobotCarState.Information,
    onClickRefresh: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(information.nameRes),
            style = MaterialTheme.typography.displayMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = DS.drawable.ic_connection),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = information.connectionDescriptionRes),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
            if (information.showRefresh) {
                TextButton(onClick = onClickRefresh) {
                    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.Control(
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit,
    onClickForward: () -> Unit,
    onClickBackward: () -> Unit,
    onClickStop: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LargeFloatingActionButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = onClickLeft,
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowLeft,
                contentDescription = null,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(SpaceBetweenButtons)) {
            LargeFloatingActionButton(
                onClick = onClickForward
            ) {
                Icon(imageVector = Icons.Rounded.KeyboardArrowUp, contentDescription = null)
            }
            LargeFloatingActionButton(
                onClick = onClickStop,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Icon(
                    painter = painterResource(id = DS.drawable.ic_remove),
                    contentDescription = null
                )
            }
            LargeFloatingActionButton(
                onClick = onClickBackward
            ) {
                Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = null)
            }
        }
        LargeFloatingActionButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = onClickRight
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = null,
            )
        }
    }
}

private val SpaceBetweenButtons = 32.dp

private const val darkThemePreview = false

@Composable
@Preview(device = "id:pixel_2")
private fun ScreenPreview() {
    HomeAppTheme(useDarkTheme = darkThemePreview) {
        RobotCarScreenContent(
            state = RobotCarState(
                information = RobotCarState.Information(
                    nameRes = R.string.device_name,
                    connectionDescriptionRes = R.string.connected_label
                )
            ),
            onClickNavigationIcon = {},
            onClickLeft = {},
            onClickRight = {},
            onClickForward = {},
            onClickBackward = {},
            onClickStop = {},
            onClickRefresh = {}
        )
    }
}