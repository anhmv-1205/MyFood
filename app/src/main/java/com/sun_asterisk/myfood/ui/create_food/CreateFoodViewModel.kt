package com.sun_asterisk.myfood.ui.create_food

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.repositories.CategoryRepository
import com.sun_asterisk.myfood.data.repositories.FoodRepository
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateFoodViewModel(
    private val foodRepository: FoodRepository,
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {


    val onCreateFoodEvent: SingleLiveEvent<ApiResponse<Food>> by lazy { SingleLiveEvent<ApiResponse<Food>>() }

    val onMessageError: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }

    fun createFood(
        categoryId: String,
        file: MultipartBody.Part,
        name: RequestBody,
        cost: RequestBody,
        unit: RequestBody
    ) {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) {
                    foodRepository.createFood(categoryId, file, name, cost, unit)
                }
                if (result.data == null) {
                    onMessageError.value = result.message
                    return@launch
                }
                onCreateFoodEvent.value = result
            } catch (ex: Exception) {
                onMessageError.value = ex.message
            }
        }
    }
}
