package com.sun_asterisk.myfood.data.datasource

import com.sun_asterisk.myfood.data.model.Order
import com.sun_asterisk.myfood.data.remote.request.CreateOrderRequest
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.OrderResponse
import com.sun_asterisk.myfood.utils.Constant

interface OrderDataSource {
    interface Remote {
        suspend fun createOrder(createOrderRequest: CreateOrderRequest): ApiResponse<Order>

        suspend fun getOrdersOfUser(page: Int = Constant.DEFAULT_PAGE): ApiResponse<OrderResponse>
    }
}
