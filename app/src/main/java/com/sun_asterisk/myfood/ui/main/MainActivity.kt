package com.sun_asterisk.myfood.ui.main

import android.os.Bundle
import com.google.android.gms.maps.MapFragment
import com.sun_asterisk.myfood.R.id
import com.sun_asterisk.myfood.R.layout
import com.sun_asterisk.myfood.base.BaseActivity
import com.sun_asterisk.myfood.ui.home.HomeFragment
import com.sun_asterisk.myfood.ui.map.MapsFragment
import com.sun_asterisk.myfood.ui.splash.SplashFragment
import com.sun_asterisk.myfood.utils.extension.addFragmentToActivity

class MainActivity : BaseActivity() {

    override fun onCreateView(savedInstanceState: Bundle?) {
        setContentView(layout.activity_main)
        init()
    }

    override fun setUpView() {
    }

    override fun bindView() {
    }

    private fun init() {
        addFragmentToActivity(
            id.containerMain,
            SplashFragment.newInstance(),
            false,
            SplashFragment::class.java.simpleName
        )
    }
}
