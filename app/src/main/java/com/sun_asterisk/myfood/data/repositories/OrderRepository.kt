package com.sun_asterisk.myfood.data.repositories

import com.sun_asterisk.myfood.data.datasource.OrderDataSource
import com.sun_asterisk.myfood.data.remote.remotedatasource.OrderRemoteDataSource
import com.sun_asterisk.myfood.data.remote.request.CreateOrderRequest
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.OrderResponse
import org.koin.core.KoinComponent

class OrderRepository(private val remote: OrderRemoteDataSource) : OrderDataSource.Remote, KoinComponent {

    override suspend fun createOrder(createOrderRequest: CreateOrderRequest) = remote.createOrder(createOrderRequest)

    override suspend fun getOrdersOfUser(page: Int) = remote.getOrdersOfUser(page)
}
