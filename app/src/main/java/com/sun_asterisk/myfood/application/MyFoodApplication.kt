package com.sun_asterisk.myfood.application

import android.app.Application
import com.sun_asterisk.myfood.di.applicationModule
import com.sun_asterisk.myfood.di.dataSourceModule
import com.sun_asterisk.myfood.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyFoodApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyFoodApplication)
            modules(listOf(applicationModule, networkModule, dataSourceModule))
        }
    }
}
