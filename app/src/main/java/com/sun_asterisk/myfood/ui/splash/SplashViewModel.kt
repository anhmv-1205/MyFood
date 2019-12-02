package com.sun_asterisk.myfood.ui.splash

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.repositories.TokenRepository

class SplashViewModel(private val tokenRepository: TokenRepository) : BaseViewModel() {

    fun checkLogin() = tokenRepository.getToken()?.isNotEmpty() ?: false
}
