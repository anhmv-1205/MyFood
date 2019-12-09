package com.sun_asterisk.myfood.di

import android.content.Context
import androidx.room.Room
import com.sun_asterisk.myfood.data.local.database.AppDatabase
import com.sun_asterisk.myfood.data.local.sharedprf.SharedPrefsImpl
import com.sun_asterisk.myfood.data.model.Order
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.repositories.CategoryRepository
import com.sun_asterisk.myfood.data.repositories.FoodRepository
import com.sun_asterisk.myfood.data.repositories.OrderRepository
import com.sun_asterisk.myfood.data.repositories.TokenRepository
import com.sun_asterisk.myfood.data.repositories.UserRepository
import com.sun_asterisk.myfood.ui.category.CategoryFragment
import com.sun_asterisk.myfood.ui.category.CategoryViewModel
import com.sun_asterisk.myfood.ui.detail_order.DetailOrderFragment
import com.sun_asterisk.myfood.ui.detail_order.DetailOrderViewModel
import com.sun_asterisk.myfood.ui.foods.FoodsFragment
import com.sun_asterisk.myfood.ui.foods.FoodsViewModel
import com.sun_asterisk.myfood.ui.home.HomeFragment
import com.sun_asterisk.myfood.ui.login.LoginFragment
import com.sun_asterisk.myfood.ui.login.LoginViewModel
import com.sun_asterisk.myfood.ui.map.MapsFragment
import com.sun_asterisk.myfood.ui.map.MapsViewModel
import com.sun_asterisk.myfood.ui.orders.OrdersFragment
import com.sun_asterisk.myfood.ui.orders.OrdersViewModel
import com.sun_asterisk.myfood.ui.profile.ProfileFragment
import com.sun_asterisk.myfood.ui.profile.ProfileViewModel
import com.sun_asterisk.myfood.ui.splash.SplashFragment
import com.sun_asterisk.myfood.ui.splash.SplashViewModel
import com.sun_asterisk.myfood.utils.Constant
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var applicationModule = module {
    // repositories
    single { UserRepository(get(), get()) }
    single { FoodRepository(get()) }
    single { TokenRepository(get()) }
    single { CategoryRepository(get()) }
    single { OrderRepository(get()) }

    // fragments
    factory { SplashFragment() }
    factory { HomeFragment() }
    factory { CategoryFragment() }
    factory { (idCategory: String) -> MapsFragment.newInstance(idCategory) }
    factory { (user: User) -> FoodsFragment.newInstance(user) }
    factory { LoginFragment() }
    factory { OrdersFragment() }
    factory { ProfileFragment() }
    factory { (order: Order) -> DetailOrderFragment.newInstance(order) }

    // ViewModel
    viewModel { CategoryViewModel(get()) }
    viewModel { MapsViewModel(get()) }
    viewModel { FoodsViewModel(get(), get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { OrdersViewModel(get()) }
    viewModel { DetailOrderViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }

    // others
    single { SharedPrefsImpl(androidContext()) }
    single { provideAppDatabase(androidContext()) }
}

fun provideAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, Constant.DATABASE_NAME).build()
}
