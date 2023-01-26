package com.eliascoelho911.homeapp.robotcar.di

import com.eliascoelho911.homeapp.robotcar.data.repository.RobotCarBluetoothRepositoryImpl
import com.eliascoelho911.homeapp.robotcar.domain.usecase.ChangeSpeedUseCase
import com.eliascoelho911.homeapp.robotcar.presentation.RobotCarViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val RobotCarModule = module {
    viewModel {
        RobotCarViewModel(
            changeSpeedUseCase = ChangeSpeedUseCase(
                repository = RobotCarBluetoothRepositoryImpl(
                    dispatcher = Dispatchers.IO
                )
            )
        )
    }
}
