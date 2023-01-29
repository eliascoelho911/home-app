package com.eliascoelho911.homeapp.robotcar.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.eliascoelho911.homeapp.robotcar.data.repository.RobotCarBluetoothRepositoryImpl
import com.eliascoelho911.homeapp.robotcar.data.service.BluetoothClientService
import com.eliascoelho911.homeapp.robotcar.domain.repository.RobotCarRepository
import com.eliascoelho911.homeapp.robotcar.domain.usecase.ConnectWithRobotUseCase
import com.eliascoelho911.homeapp.robotcar.domain.usecase.UpdateSpeedUseCase
import com.eliascoelho911.homeapp.robotcar.infrastructure.BluetoothDeviceManager
import com.eliascoelho911.homeapp.robotcar.infrastructure.Constants
import com.eliascoelho911.homeapp.robotcar.ui.RobotCarViewModel
import java.util.UUID
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val RobotCarModule = module {
    viewModel {
        RobotCarViewModel(
            updateSpeedUseCase = UpdateSpeedUseCase(
                repository = get()
            ),
            connectWithRobotUseCase = ConnectWithRobotUseCase(
                repository = get()
            )
        )
    }

    factory<RobotCarRepository> {
        RobotCarBluetoothRepositoryImpl(
            bluetoothService = get()
        )
    }

    single {
        BluetoothClientService(
            deviceManager = get(),
            uuid = UUID.fromString(Constants.UUID)
        )
    }

    single(createdAtStart = true) { BluetoothDeviceManager(getBluetoothAdapter(get())) }
}

private fun getBluetoothAdapter(context: Context): BluetoothAdapter {
    val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    return bluetoothManager.adapter
}
