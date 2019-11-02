package com.sun_asterisk.myfood.ui.category

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.data.remote.api.error.RetrofitException
import com.sun_asterisk.myfood.data.repositories.CategoryRepository
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryViewModel(private val categoryRepository: CategoryRepository) : BaseViewModel() {

    val onCategoryEvent: SingleLiveEvent<MutableList<Category>> by lazy { SingleLiveEvent<MutableList<Category>>() }

    val onMessageError: SingleLiveEvent<RetrofitException> by lazy { SingleLiveEvent<RetrofitException>() }

    fun getCategories() {
        coroutineScope.launch(Dispatchers.Main) {
            var result = mutableListOf<Category>()
            withContext(Dispatchers.IO) {
                result = categoryRepository.getCategory()
            }
            onCategoryEvent.value = result
        }

//        coroutineScope.launch(Dispatchers.IO) {
//            onCategoryEvent.value = categoryRepository.getCategory()
//        }
    }

    override fun printException(throwable: Throwable) {
        if (throwable is RetrofitException) onMessageError.value = throwable
    }
}
