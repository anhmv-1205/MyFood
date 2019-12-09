package com.sun_asterisk.myfood.ui.profile

import androidx.lifecycle.LiveData
import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.repositories.TokenRepository
import com.sun_asterisk.myfood.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val tokenRepository: TokenRepository, private val userRepository: UserRepository) :
    BaseViewModel() {

    val onUserEvent: LiveData<User> = userRepository.getUser()

    fun logout() {
        coroutineScope.launch(Dispatchers.IO) {
            tokenRepository.clearToken()
            userRepository.clearUser()
        }
    }
}
