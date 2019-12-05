package com.sun_asterisk.myfood.ui.orders

import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.model.Order
import com.sun_asterisk.myfood.data.repositories.OrderRepository
import com.sun_asterisk.myfood.utils.Constant
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersViewModel(private val ordersRepository: OrderRepository) : BaseViewModel() {

    val onGetOrdersEvent: SingleLiveEvent<MutableList<Order>> by lazy { SingleLiveEvent<MutableList<Order>>() }

    val onProgressDialogEvent: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }

    val onMessageErrorEvent: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }

    fun getOrdersOfUser(page: Int = Constant.DEFAULT_PAGE) {
        coroutineScope.launch(Dispatchers.Main) {
            onProgressDialogEvent.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    ordersRepository.getOrdersOfUser(page)
                }
                if (result.data == null) {
                    onMessageErrorEvent.value = result.message
                    return@launch
                }
                onGetOrdersEvent.value = result.data.orders
            } catch (ex: Exception) {
                println(ex.message)
                onMessageErrorEvent.value = ex.message
            }
            onProgressDialogEvent.value = false
        }
    }
}
