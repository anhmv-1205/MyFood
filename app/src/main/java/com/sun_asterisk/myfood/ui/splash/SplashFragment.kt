package com.sun_asterisk.myfood.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.ui.home.HomeFragment
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.replaceFragment
import org.koin.android.ext.android.inject

class SplashFragment : BaseFragment() {

    private val homeFragment: HomeFragment by inject()

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        onHideSoftKeyBoard()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun setUpView() {
        Handler().postDelayed({
            replaceFragment(
                R.id.containerMain,
                homeFragment,
                false,
                HomeFragment::class.java.simpleName
            )
        }, Constant.TIME_DELAY)
    }

    override fun bindView() {
    }
}
