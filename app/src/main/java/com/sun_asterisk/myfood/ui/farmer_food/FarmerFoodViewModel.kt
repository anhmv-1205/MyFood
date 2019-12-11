package com.sun_asterisk.myfood.ui.farmer_food

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.data.repositories.CategoryRepository
import com.sun_asterisk.myfood.data.repositories.FoodRepository
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FarmerFoodViewModel(
    private val foodRepository: FoodRepository,
    private val categoryRepository: CategoryRepository
) : BaseViewModel() {

    val onGetFoodsOfFarmerEvent: SingleLiveEvent<MutableList<Food>> by lazy { SingleLiveEvent<MutableList<Food>>() }

    val onMessageErrorEvent: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }

    val onCategoryEvent: SingleLiveEvent<MutableList<Category>> by lazy { SingleLiveEvent<MutableList<Category>>() }

    fun getCategories() {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) {
                    categoryRepository.getCategory()
                }
                onCategoryEvent.value = result
            } catch (ex: java.lang.Exception) {
                onMessageErrorEvent.value = ex.message
            }
        }
    }

    fun getFoodsOfUser(page: Int = Constant.DEFAULT_PAGE) {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) {
                    foodRepository.getFoodsOfUser(page)
                }
                if (result.data == null) {
                    onMessageErrorEvent.value = result.message
                    return@launch
                }
                onGetFoodsOfFarmerEvent.value = result.data.foods
            } catch (ex: Exception) {
                onMessageErrorEvent.value = ex.message
            }
        }
    }
}
