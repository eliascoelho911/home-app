package com.eliascoelho911.homeapp.robotcar.presentation

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliascoelho911.homeapp.robotcar.data.RobotCarConstants
import com.eliascoelho911.homeapp.robotcar.domain.model.Speed
import com.eliascoelho911.homeapp.robotcar.domain.usecase.UpdateSpeedUseCase
import java.util.UUID
import kotlinx.coroutines.launch

internal class RobotCarViewModel(
    private val updateSpeedUseCase: UpdateSpeedUseCase,
) : ViewModel() {

    private val _state = MutableLiveData(initialState)

    val state: LiveData<RobotCarState> get() = _state

    private val _actions = MutableLiveData<RobotCarAction>()

    val actions: LiveData<RobotCarAction> get() = _actions

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun onLeftWheelSpeedChanged(context: Context, speed: Speed) {
        updateControlLeftValue(context, speed)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun updateControlLeftValue(context: Context, leftValue: Float) {
        viewModelScope.launch {
            updateSpeedUseCase.updateLeftWheelSpeed(
                speed = leftValue,
                bluetoothSocket = getOrCreateBluetoothSocket(context)
            )
        }

        setState {
            it.copy(
                control = it.control.copy(leftValue = leftValue)
            )
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun onRightWheelSpeedChanged(context: Context, speed: Speed) {
        updateControlRightValue(context, speed)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun updateControlRightValue(context: Context, rightValue: Float) {
        viewModelScope.launch {
            updateSpeedUseCase.updateRightWheelVelocity(
                speed = rightValue,
                bluetoothSocket = getOrCreateBluetoothSocket(context)
            )
        }

        setState {
            it.copy(
                control = it.control.copy(rightValue = rightValue)
            )
        }
    }

    fun requestPermission(permission: String) {
        sendAction(RobotCarAction.RequestPermission(permission))
    }

    private fun sendAction(action: RobotCarAction) {
        _actions.value = action
    }

    private fun setState(state: (currentState: RobotCarState) -> RobotCarState) {
        _state.value?.let { currentState ->
            _state.value = state(currentState)
        }
    }

    private var bluetoothSocket: BluetoothSocket? = null

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun getOrCreateBluetoothSocket(context: Context): BluetoothSocket {
        runCatching {
            if (bluetoothSocket == null) {
                val bluetoothManager =
                    context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                val bluetoothAdapter = bluetoothManager.adapter
                val device = bluetoothAdapter.getRemoteDevice(RobotCarConstants.MAC_ADDRESS)
                val uuid = UUID.fromString(RobotCarConstants.INSECURE_UUID)
                bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(uuid)
            }
        }.onFailure {
            showError()
        }

        return bluetoothSocket!!
    }

    private fun showError() {
        TODO("Not yet implemented")
    }
}