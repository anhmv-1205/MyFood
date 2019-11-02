package com.sun_asterisk.myfood.di

import android.content.Context
import androidx.room.Room
import com.sun_asterisk.myfood.data.local.database.AppDatabase
import com.sun_asterisk.myfood.data.local.sharedprf.SharedPrefsImpl
import com.sun_asterisk.myfood.data.repositories.CategoryRepository
import com.sun_asterisk.myfood.data.repositories.FoodRepository
import com.sun_asterisk.myfood.data.repositories.TokenRepository
import com.sun_asterisk.myfood.data.repositories.UserRepository
import com.sun_asterisk.myfood.ui.category.CategoryFragment
import com.sun_asterisk.myfood.ui.category.CategoryViewModel
import com.sun_asterisk.myfood.ui.home.HomeFragment
import com.sun_asterisk.myfood.ui.map.MapsFragment
import com.sun_asterisk.myfood.ui.splash.SplashFragment
import com.sun_asterisk.myfood.utils.Constant
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var applicationModule = module {
    // repositories
    single { UserRepository(get(), get()) }
    single { FoodRepository }
    single { TokenRepository(get()) }
    single { CategoryRepository(get()) }

    // fragments
    factory { SplashFragment() }
    factory { HomeFragment() }
    factory { CategoryFragment() }
    factory { (idCategory: String) -> MapsFragment.newInstance(idCategory) }

    // ViewModel
    viewModel { CategoryViewModel(get()) }

    // others
    single { SharedPrefsImpl(androidContext()) }
    single { provideAppDatabase(androidContext()) }
}

fun provideAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, Constant.DATABASE_NAME).build()
}
