package com.sun_asterisk.myfood.ui.home

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.repositories.UserRepository
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val onGetRoleOfUserEvent: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>() }

    fun getRoleOfUser() {
        coroutineScope.launch(Dispatchers.Main) {
            onGetRoleOfUserEvent.value = withContext(Dispatchers.IO) {
                userRepository.getRoleOfUser()
            }
        }
    }
}
