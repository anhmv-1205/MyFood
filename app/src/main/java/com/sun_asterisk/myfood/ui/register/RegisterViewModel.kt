package com.sun_asterisk.myfood.ui.register

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.remote.request.RegisterRequest
import com.sun_asterisk.myfood.data.repositories.UserRepository
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val onRegisterEvent: SingleLiveEvent<User> by lazy { SingleLiveEvent<User>() }

    val onMessageErrorEvent: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }

    fun register(registerRequest: RegisterRequest) {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) {
                    userRepository.register(registerRequest)
                }
                if (result.data == null) {
                    onMessageErrorEvent.value = result.message
                    return@launch
                }
                onRegisterEvent.value = result.data
            } catch (ex: Exception) {
                onMessageErrorEvent.value = ex.message
            }
        }
    }
}
