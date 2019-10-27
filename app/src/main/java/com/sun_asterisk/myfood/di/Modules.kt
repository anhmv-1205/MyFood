package com.sun_asterisk.myfood.di

import com.sun_asterisk.myfood.data.repositories.FoodRepository
import com.sun_asterisk.myfood.data.repositories.UserRepository
import com.sun_asterisk.myfood.ui.category.CategoryFragment
import com.sun_asterisk.myfood.ui.home.HomeFragment
import com.sun_asterisk.myfood.ui.map.MapsFragment
import com.sun_asterisk.myfood.ui.splash.SplashFragment
import org.koin.dsl.module

var applicationModule = module {
    // repositories
    single { UserRepository }
    single { FoodRepository }

    // fragments
    factory { SplashFragment() }
    factory { HomeFragment() }
    factory { CategoryFragment() }
    factory { (idCategory: String) -> MapsFragment.newInstance(idCategory) }
}
