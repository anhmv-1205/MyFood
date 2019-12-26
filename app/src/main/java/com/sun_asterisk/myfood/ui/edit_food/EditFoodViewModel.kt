package com.sun_asterisk.myfood.ui.edit_food

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.data.repositories.FoodRepository
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditFoodViewModel(private val foodRepository: FoodRepository) : BaseViewModel() {
    val onEditFoodEvent: SingleLiveEvent<Food> by lazy { SingleLiveEvent<Food>() }

    val onDialogProgressEvent: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }

    val onMessageErrorEvent: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }

    fun editFood(
        foodId: String,
        categoryId: RequestBody,
        file: MultipartBody.Part?,
        name: RequestBody,
        cost: RequestBody,
        unit: RequestBody
    ) {
        coroutineScope.launch(Dispatchers.Main) {
//            onDialogProgressEvent.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    foodRepository.editFood(foodId, categoryId, file, name, cost, unit)
                }
                if (result.data == null) {
                    onMessageErrorEvent.value = result.message
//                    onDialogProgressEvent.value = false
                    return@launch
                }
                onEditFoodEvent.value = result.data
            } catch (exception: Exception) {
                onMessageErrorEvent.value = exception.message
            }
//            onDialogProgressEvent.value = false
        }
    }
}
