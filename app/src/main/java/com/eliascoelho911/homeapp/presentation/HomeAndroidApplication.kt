package com.eliascoelho911.homeapp.presentation

import android.app.Application
import com.eliascoelho911.homeapp.robotcar.di.RobotCarModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

internal class HomeAndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HomeAndroidApplication)
            modules(RobotCarModule)
        }
    }
}