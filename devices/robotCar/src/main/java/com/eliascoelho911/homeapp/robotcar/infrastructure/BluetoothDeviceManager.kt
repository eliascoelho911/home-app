package com.eliascoelho911.homeapp.robotcar.infrastructure

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.annotation.RequiresPermission

class BluetoothDeviceManager(
    private val bluetoothAdapter: BluetoothAdapter
) {
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun getDeviceOrNull(): BluetoothDevice? {
        val device = bluetoothAdapter.getRemoteDevice(Constants.MAC_ADDRESS)

        if (device == null) log("Device ${Constants.MAC_ADDRESS} not founded")

        return device
    }

    private fun log(msg: String) =
        Log.d("BluetoothDeviceManager", msg)
}