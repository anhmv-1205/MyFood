package com.sun_asterisk.myfood.ui.foods

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.remote.response.FoodResponse
import com.sun_asterisk.myfood.data.repositories.FoodRepository
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodsViewModel(private val repository: FoodRepository) : BaseViewModel() {

    val onGetFoodEvent: SingleLiveEvent<FoodResponse> by lazy {
        SingleLiveEvent<FoodResponse>()
    }

    val onMessageError: SingleLiveEvent<String> by lazy {
        SingleLiveEvent<String>()
    }

    val onProgressEvent: SingleLiveEvent<Boolean> by lazy {
        SingleLiveEvent<Boolean>()
    }

    fun getFoodsWithUserId(userId: String, page: Int = Constant.DEFAULT_PAGE) {
        coroutineScope.launch(Dispatchers.Main) {
            onProgressEvent.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getFoodsWithIdUser(userId, page)
                }
                if (result.data == null) {
                    onMessageError.value = result.message
                    return@launch
                }
                onGetFoodEvent.value = result.data
            } catch (exception: Exception) {
                println(exception)
                onMessageError.value = exception.message
            }
            onProgressEvent.value = false
        }
    }
}
