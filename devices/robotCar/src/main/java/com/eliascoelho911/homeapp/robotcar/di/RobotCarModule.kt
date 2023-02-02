package com.eliascoelho911.homeapp.robotcar.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.eliascoelho911.homeapp.commons.service.BluetoothClientService
import com.eliascoelho911.homeapp.robotcar.data.cache.RobotLocalStorage
import com.eliascoelho911.homeapp.robotcar.data.repository.RobotBluetoothRepositoryImpl
import com.eliascoelho911.homeapp.robotcar.data.service.RobotBluetoothService
import com.eliascoelho911.homeapp.robotcar.domain.repository.RobotRepository
import com.eliascoelho911.homeapp.robotcar.domain.usecase.ConnectWithRobotUseCase
import com.eliascoelho911.homeapp.robotcar.domain.usecase.UpdateRobotUseCase
import com.eliascoelho911.homeapp.robotcar.infrastructure.BluetoothDeviceManager
import com.eliascoelho911.homeapp.robotcar.infrastructure.Constants
import com.eliascoelho911.homeapp.robotcar.presentation.RobotViewModel
import java.util.UUID
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val RobotCarModule = module {
    viewModel {
        RobotViewModel(
            updateRobotUseCase = UpdateRobotUseCase(
                repository = get()
            ),
            connectWithRobotUseCase = ConnectWithRobotUseCase(
                repository = get()
            )
        )
    }

    factory<RobotRepository> {
        RobotBluetoothRepositoryImpl(
            robotBluetoothService = RobotBluetoothService(
                bluetoothClientService = get(),
                bluetoothDeviceManager = get()
            ),
            robotLocalStorage = get()
        )
    }

    single {
        BluetoothClientService(
            uuid = UUID.fromString(Constants.UUID)
        )
    }

    single { RobotLocalStorage() }

    single { BluetoothDeviceManager(getBluetoothAdapter(get())) }
}

private fun getBluetoothAdapter(context: Context): BluetoothAdapter {
    val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    return bluetoothManager.adapter
}
