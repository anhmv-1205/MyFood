package com.sun_asterisk.myfood.ui.login

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.remote.request.SignInRequest
import com.sun_asterisk.myfood.data.repositories.TokenRepository
import com.sun_asterisk.myfood.data.repositories.UserRepository
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val tokenRepository: TokenRepository, private val userRepository: UserRepository) :
    BaseViewModel() {

    val onLoginEvent: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }

    val onProgressDialogEvent: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }

    val onMessageError: SingleLiveEvent<Exception> by lazy { SingleLiveEvent<Exception>() }

    fun login(signInRequest: SignInRequest) {
        coroutineScope.launch(Dispatchers.Main) {
//            onProgressDialogEvent.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    userRepository.signIn(signInRequest)
                }
                if (result.user == null || result.token == null) {
//                    onProgressDialogEvent.value = false
                    onMessageError.value = Exception(result.message)
                    return@launch
                }
                tokenRepository.saveToken(result.token)
                withContext(Dispatchers.IO) {
                    userRepository.insertUser(result.user)
                }
                onLoginEvent.value = true
            } catch (ex: Exception) {
                onMessageError.value = ex
//                onProgressDialogEvent.value = false
            }
        }
    }
}
