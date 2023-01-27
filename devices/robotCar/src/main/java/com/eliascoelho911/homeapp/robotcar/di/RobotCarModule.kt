package com.eliascoelho911.homeapp.robotcar.di

import com.eliascoelho911.homeapp.robotcar.data.repository.RobotCarBluetoothRepositoryImpl
import com.eliascoelho911.homeapp.robotcar.domain.usecase.UpdateSpeedUseCase
import com.eliascoelho911.homeapp.robotcar.presentation.RobotCarViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val RobotCarModule = module {
    viewModel {
        RobotCarViewModel(
            updateSpeedUseCase = UpdateSpeedUseCase(
                repository = RobotCarBluetoothRepositoryImpl(
                    dispatcher = Dispatchers.IO
                )
            )
        )
    }
}
