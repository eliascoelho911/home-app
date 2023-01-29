package com.eliascoelho911.homeapp.robotcar.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliascoelho911.homeapp.robotcar.R
import com.eliascoelho911.homeapp.robotcar.domain.usecase.ConnectWithRobotUseCase
import com.eliascoelho911.homeapp.robotcar.domain.usecase.UpdateSpeedUseCase
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.launch

internal class RobotCarViewModel(
    private val updateSpeedUseCase: UpdateSpeedUseCase,
    private val connectWithRobotUseCase: ConnectWithRobotUseCase
) : ViewModel() {

    private val _state = MutableLiveData(initialState)

    val state: LiveData<RobotCarState> get() = _state

    private val _actions = MutableLiveData<RobotCarAction>()

    val actions: LiveData<RobotCarAction> get() = _actions

    init {
        onConnectWithRobot()
    }

    private fun onConnectWithRobot() {
        viewModelScope.launch {
            runCatching {
                setInformationState {
                    it.copy(
                        showRefresh = false,
                        connectionDescriptionRes = R.string.connecting_label
                    )
                }
                connectWithRobotUseCase()
            }.onFailure {
                setInformationState {
                    it.copy(
                        connectionDescriptionRes = R.string.disconnected_label,
                        showRefresh = true
                    )
                }
            }.onSuccess {
                setInformationState {
                    it.copy(
                        connectionDescriptionRes = R.string.connected_label
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun onClickRight(permissionState: PermissionState) {
        safeRight(permissionState)
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun onClickLeft(permissionState: PermissionState) {
        safeLeft(permissionState)
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun onClickForward(permissionState: PermissionState) {
        safeForward(permissionState)
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun onClickBackward(permissionState: PermissionState) {
        safeBackward(permissionState)
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun onClickStop(permissionState: PermissionState) {
        safeStop(permissionState)
    }

    fun onClickRefresh() {
        onConnectWithRobot()
    }

    @OptIn(ExperimentalPermissionsApi::class)
    private fun safeRight(
        permissionState: PermissionState,
    ) {
        runGrantingPermission(permissionState) {
            viewModelScope.launch {
                updateSpeedUseCase.right()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    private fun safeLeft(
        permissionState: PermissionState,
    ) {
        runGrantingPermission(permissionState) {
            viewModelScope.launch {
                updateSpeedUseCase.left()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    private fun safeForward(
        permissionState: PermissionState,
    ) {
        runGrantingPermission(permissionState) {
            viewModelScope.launch {
                updateSpeedUseCase.forward()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    private fun safeBackward(
        permissionState: PermissionState,
    ) {
        runGrantingPermission(permissionState) {
            viewModelScope.launch {
                updateSpeedUseCase.backward()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    private fun safeStop(
        permissionState: PermissionState,
    ) {
        runGrantingPermission(permissionState) {
            viewModelScope.launch {
                updateSpeedUseCase.stop()
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    private fun runGrantingPermission(permissionState: PermissionState, block: () -> Unit) {
        if (permissionState.status.isGranted) block() else requestPermission(permissionState.permission)
    }

    private fun requestPermission(permission: String) {
        sendAction(RobotCarAction.RequestPermission(permission))
    }

    private fun sendAction(action: RobotCarAction) {
        _actions.value = action
    }

    private fun setInformationState(
        informationState: (currentState: RobotCarState.Information) -> RobotCarState.Information
    ) {
        setState {
            it.copy(
                information = informationState(it.information)
            )
        }
    }

    private fun setState(state: (currentState: RobotCarState) -> RobotCarState) {
        _state.value?.let { currentState ->
            _state.value = state(currentState)
        }
    }
}