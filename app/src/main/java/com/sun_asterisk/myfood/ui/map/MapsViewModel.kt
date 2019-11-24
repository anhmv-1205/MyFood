package com.sun_asterisk.myfood.ui.map

import androidx.lifecycle.MutableLiveData
import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.repositories.UserRepository
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val onUsersEvent: SingleLiveEvent<MutableList<User>> by lazy { SingleLiveEvent<MutableList<User>>() }

    val onMessageError: MutableLiveData<Exception> by lazy { MutableLiveData<Exception>() }

    val onGetNumbersOfFoodByUserId: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    fun getUserByCategoryId(categoryId: String) {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) {
                    userRepository.getUsersWithCategoryId(categoryId)
                }
                onUsersEvent.value = result
            } catch (exception: Exception) {
                onMessageError.value = exception
            }
        }
    }

    fun getNumbersFoodByUserId(userId: String) {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) {
                    userRepository.getNumbersOfFoodByUserId(userId)
                }
                onGetNumbersOfFoodByUserId.value = result
            } catch (exception: Exception) {
                onMessageError.value = exception
            }
        }
    }
}
