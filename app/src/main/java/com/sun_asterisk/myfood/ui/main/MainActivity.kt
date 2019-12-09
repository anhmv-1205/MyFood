package com.sun_asterisk.myfood.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.sun_asterisk.myfood.R
import com.sun_asterisk.myfood.R.id
import com.sun_asterisk.myfood.R.layout
import com.sun_asterisk.myfood.base.BaseActivity
import com.sun_asterisk.myfood.ui.login.LoginFragment
import com.sun_asterisk.myfood.ui.splash.SplashFragment
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.extension.addFragmentToActivity
import com.sun_asterisk.myfood.utils.extension.delayTask
import com.sun_asterisk.myfood.utils.extension.goBackFragment
import com.sun_asterisk.myfood.utils.extension.showToast
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity(), OnActionBarListener, OnCallBackLogoutListener {

    private val splashFragment: SplashFragment by inject()
    private var isDoubleTapBack = false

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                goBackFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setupActionBar(toolbar: Toolbar, isShowBackIcon: Boolean) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(isShowBackIcon)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onBackPressed() {
        if (goBackFragment()) return
        if (isDoubleTapBack) {
            finish()
            return
        }
        isDoubleTapBack = true
        showToast(getString(R.string.text_click_again_to_exit))
        delayTask({
            isDoubleTapBack = false
        }, Constant.MAX_TIME_DOUBLE_CLICK_EXIT)
    }

    override fun onLogoutReceived() {
        val loginFragment: LoginFragment by inject()
        addFragmentToActivity(
            id.containerMain,
            loginFragment,
            false,
            LoginFragment::class.java.simpleName
        )
    }
}
