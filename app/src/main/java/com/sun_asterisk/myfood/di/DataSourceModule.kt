package com.sun_asterisk.myfood.di

import com.sun_asterisk.myfood.data.local.database.AppDatabase
import com.sun_asterisk.myfood.data.local.localdatasource.UserLocalDataSource
import com.sun_asterisk.myfood.data.local.sharedprf.SharedPrefApi
import com.sun_asterisk.myfood.data.local.sharedprf.SharedPrefsImpl
import com.sun_asterisk.myfood.data.remote.remotedatasource.CategoryRemoteDataSource
import com.sun_asterisk.myfood.data.remote.remotedatasource.FoodRemoteDataSource
import com.sun_asterisk.myfood.data.remote.remotedatasource.OrderRemoteDataSource
import com.sun_asterisk.myfood.data.remote.remotedatasource.UserRemoteDataSource
import org.koin.dsl.module

val dataSourceModule = module {
    single { UserRemoteDataSource(get()) }
    single { CategoryRemoteDataSource(get()) }
    single { provideUserLocalDataSource(get(), get()) }
    single { provideSharedPrefApi(get()) }
    single { FoodRemoteDataSource(get()) }
    single { OrderRemoteDataSource(get()) }
}

fun provideUserLocalDataSource(sharedPrefApi: SharedPrefApi, appDatabase: AppDatabase): UserLocalDataSource {
    return UserLocalDataSource(sharedPrefApi, appDatabase.userDao())
}

fun provideSharedPrefApi(sharedPrefsImpl: SharedPrefsImpl): SharedPrefApi = sharedPrefsImpl
