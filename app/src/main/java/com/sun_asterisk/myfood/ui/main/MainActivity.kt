package com.sun_asterisk.myfood.ui.main

import android.os.Bundle
import com.sun_asterisk.myfood.R.id
import com.sun_asterisk.myfood.R.layout
import com.sun_asterisk.myfood.base.BaseActivity
import com.sun_asterisk.myfood.ui.splash.SplashFragment
import com.sun_asterisk.myfood.utils.extension.addFragmentToActivity
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {

    private val splashFragment: SplashFragment by inject()

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
            splashFragment,
            false,
            SplashFragment::class.java.simpleName
        )
    }
}
