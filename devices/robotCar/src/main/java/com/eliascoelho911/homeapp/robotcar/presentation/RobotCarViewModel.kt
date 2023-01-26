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
import com.eliascoelho911.homeapp.robotcar.domain.usecase.ChangeSpeedUseCase
import java.util.UUID
import kotlinx.coroutines.launch

internal class RobotCarViewModel(
    private val changeSpeedUseCase: ChangeSpeedUseCase,
) : ViewModel() {

    private val _state = MutableLiveData<RobotCarState>()

    val state: LiveData<RobotCarState> get() = _state

    private val _actions = MutableLiveData<RobotCarAction>()

    val actions: LiveData<RobotCarAction> get() = _actions

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun onLeftWheelVelocityChanged(context: Context, speed: Speed) {
        viewModelScope.launch {
            updateControlLeftValue(context, speed)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private suspend fun updateControlLeftValue(context: Context, leftValue: Float) {
        changeSpeedUseCase.updateLeftWheelSpeed(
            speed = leftValue,
            bluetoothSocket = getOrCreateBluetoothSocket(context)
        )

        setState {
            it.copy(
                control = it.control.copy(leftValue = leftValue)
            )
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun onRightWheelVelocityChanged(context: Context, speed: Speed) {
        viewModelScope.launch {
            updateControlRightValue(context, speed)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private suspend fun updateControlRightValue(context: Context, rightValue: Float) {
        changeSpeedUseCase.updateRightWheelVelocity(
            speed = rightValue,
            bluetoothSocket = getOrCreateBluetoothSocket(context)
        )

        setState {
            it.copy(
                control = it.control.copy(rightValue = rightValue)
            )
        }
    }

    fun onRequestPermission(permission: String) {
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