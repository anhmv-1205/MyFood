package com.sun_asterisk.myfood.data.remote.remotedatasource

import com.sun_asterisk.myfood.data.datasource.OrderDataSource
import com.sun_asterisk.myfood.data.remote.api.service.MyFoodApi
import com.sun_asterisk.myfood.data.remote.request.CreateOrderRequest

class OrderRemoteDataSource(private val myFoodApi: MyFoodApi) : OrderDataSource.Remote {

    override suspend fun createOrder(createOrderRequest: CreateOrderRequest) = myFoodApi.createOrder(createOrderRequest)
}
