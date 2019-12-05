package com.sun_asterisk.myfood.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.base.BaseFragment
import com.sun_asterisk.myfood.ui.home.HomeFragment
import com.sun_asterisk.myfood.ui.login.LoginFragment
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.replaceFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment() {

    private val homeFragment: HomeFragment by inject()
    private val loginFragment: LoginFragment by inject()
    private val viewModel: SplashViewModel by viewModel()

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        onHideSoftKeyBoard()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun setUpView() {
        if (viewModel.checkLogin()) replaceFragment(homeFragment, HomeFragment::class.java.simpleName)
        else replaceFragment(loginFragment, LoginFragment::class.java.simpleName)
    }

    private fun replaceFragment(fragment: Fragment, name: String) {
        Handler().postDelayed({
            replaceFragment(R.id.containerMain, fragment, false, name)
        }, Constant.TIME_DELAY)
    }

    override fun registerLiveData() {
    }

    override fun bindView() {
    }
}
