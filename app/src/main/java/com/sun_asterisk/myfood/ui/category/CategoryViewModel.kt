package com.sun_asterisk.myfood.ui.category

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.data.repositories.CategoryRepository
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryViewModel(private val categoryRepository: CategoryRepository) : BaseViewModel() {

    val onCategoryEvent: SingleLiveEvent<MutableList<Category>> by lazy { SingleLiveEvent<MutableList<Category>>() }

    val onMessageError: SingleLiveEvent<Exception> by lazy { SingleLiveEvent<Exception>() }

    fun getCategories() {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) {
                    categoryRepository.getCategory()
                }
                onCategoryEvent.value = result
            } catch (ex: java.lang.Exception) {
                onMessageError.value = ex
            }
        }
    }
}
