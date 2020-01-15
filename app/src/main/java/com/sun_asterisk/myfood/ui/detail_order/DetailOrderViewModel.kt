package com.sun_asterisk.myfood.ui.detail_order

import androidx.lifecycle.LiveData
import com.sun_asterisk.myfood.base.BaseViewModel
import com.sun_asterisk.myfood.data.model.Order
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.remote.request.CreateOrderRequest
import com.sun_asterisk.myfood.data.remote.request.RatingRequest
import com.sun_asterisk.myfood.data.repositories.CommentRepository
import com.sun_asterisk.myfood.data.repositories.OrderRepository
import com.sun_asterisk.myfood.data.repositories.UserRepository
import com.sun_asterisk.myfood.utils.livedata.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailOrderViewModel(
    private val userRepository: UserRepository,
    private val orderRepository: OrderRepository,
    private val commentRepository: CommentRepository
) :
    BaseViewModel() {

    val onOwnerEvent: LiveData<User> = userRepository.getUser()

    val onUserEvent: SingleLiveEvent<User> by lazy { SingleLiveEvent<User>() }

    val onRefreshOrderEvent: SingleLiveEvent<Order> by lazy { SingleLiveEvent<Order>() }

    val onMessageErrorEvent: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }

    val onProgressDialogEvent: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }

    val onCreateOrderEvent: SingleLiveEvent<Order> by lazy { SingleLiveEvent<Order>() }

    val onCreateCommentEvent: SingleLiveEvent<Boolean> by lazy { SingleLiveEvent<Boolean>() }

    fun getFarmerOrBuyer(userId: String) {
        coroutineScope.launch(Dispatchers.Main) {
            onProgressDialogEvent.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    userRepository.getUserByUserId(userId)
                }
                if (result.data == null) {
                    onMessageErrorEvent.value = result.message
                    onProgressDialogEvent.value = false
                }
                onUserEvent.value = result.data
            } catch (ex: Exception) {
                onMessageErrorEvent.value = ex.message
            }
            onProgressDialogEvent.value = false
        }
    }

    fun refreshOrder(orderId: String) {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) {
                    orderRepository.getOrderByOrderId(orderId)
                }
                if (result.data == null) {
                    onMessageErrorEvent.value = result.message
                    return@launch
                }
                onRefreshOrderEvent.value = result.data
            } catch (ex: Exception) {
                onMessageErrorEvent.value = ex.message
            }
        }
    }

    fun updateOrderStatus(orderId: String, toStatus: String) {
        coroutineScope.launch(Dispatchers.Main) {
            onProgressDialogEvent.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    orderRepository.updateOrderStatus(orderId, toStatus)
                }
                if (result.data == null) {
                    onProgressDialogEvent.value = false
                    onMessageErrorEvent.value = result.message
                    return@launch
                }
                onRefreshOrderEvent.value = result.data
            } catch (ex: Exception) {
                onMessageErrorEvent.value = ex.message
            }
            onProgressDialogEvent.value = false
        }
    }

    fun createOrder(createOrderRequest: CreateOrderRequest) {
        coroutineScope.launch(Dispatchers.Main) {
            onProgressDialogEvent.value = true
            try {
                val result = withContext(Dispatchers.IO) {
                    orderRepository.createOrder(createOrderRequest)
                }
                if (result.data == null) {
                    onMessageErrorEvent.value = result.message
                    onProgressDialogEvent.value = false
                    return@launch
                }
                onCreateOrderEvent.value = result.data
                onProgressDialogEvent.value = false
            } catch (ex: java.lang.Exception) {
                onProgressDialogEvent.value = false
                onMessageErrorEvent.value = ex.message
            }
        }
    }

    fun createRating(ratingRequest: RatingRequest) {
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val rs = withContext(Dispatchers.IO) {
                    commentRepository.createComment(ratingRequest)
                }
                if (rs.data == null) {
                    onMessageErrorEvent.value = rs.message
                    return@launch
                }
                onCreateCommentEvent.value = true
            } catch (ex: Exception) {
                onMessageErrorEvent.value = ex.message
            }
        }
    }
}
