package com.eliascoelho911.homeapp.robotcar.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliascoelho911.homeapp.robotcar.domain.model.Robot
import com.eliascoelho911.homeapp.robotcar.domain.usecase.ConnectWithRobotUseCase
import com.eliascoelho911.homeapp.robotcar.domain.usecase.UpdateRobotUseCase
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.launch

internal class RobotViewModel(
    private val updateRobotUseCase: UpdateRobotUseCase,
    private val connectWithRobotUseCase: ConnectWithRobotUseCase
) : ViewModel() {

    private val _state = MutableLiveData(initialState)

    val state: LiveData<RobotCarState> get() = _state

    private val _actions = MutableLiveData<RobotAction>()

    val actions: LiveData<RobotAction> get() = _actions

    init {
        onConnectWithRobot()
    }

    private fun onConnectWithRobot() {
        viewModelScope.launch {
            runCatching {
                setState { it.connectingState }
                connectWithRobotUseCase()
            }.onFailure {
                setState { it.disconnectedState }
            }.onSuccess {
                setState { it.connectedState }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun onClickRight(permissionState: PermissionState) {
        updateRobotGrantingPermission(permissionState = permissionState) {
            right()
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun onClickLeft(permissionState: PermissionState) {
        updateRobotGrantingPermission(permissionState = permissionState) {
            left()
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun onClickForward(permissionState: PermissionState) {
        updateRobotGrantingPermission(permissionState = permissionState) {
            forward()
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun onClickBackward(permissionState: PermissionState) {
        updateRobotGrantingPermission(permissionState = permissionState) {
            backward()
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun onClickStop(permissionState: PermissionState) {
        updateRobotGrantingPermission(permissionState = permissionState) {
            stop()
        }
    }

    fun onClickRefresh() {
        onConnectWithRobot()
    }

    @OptIn(ExperimentalPermissionsApi::class)
    private fun updateRobotGrantingPermission(
        permissionState: PermissionState,
        robot: Robot.() -> Robot
    ) {
        runGrantingPermission(permissionState, onGranted = {
            viewModelScope.launch {
                updateRobotUseCase(robot)
            }
        }, onDenied = { requestPermission(it) })
    }

    private fun requestPermission(permission: String) {
        sendAction(RobotAction.RequestPermission(permission))
    }

    private fun sendAction(action: RobotAction) {
        _actions.value = action
    }

    private fun setState(state: (currentState: RobotCarState) -> RobotCarState) {
        _state.value?.let { currentState ->
            _state.value = state(currentState)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun runGrantingPermission(
    permissionState: PermissionState,
    onGranted: () -> Unit,
    onDenied: (permission: String) -> Unit
) {
    if (permissionState.status.isGranted) onGranted() else onDenied(permissionState.permission)
}