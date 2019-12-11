package com.sun_asterisk.myfood.data.remote.remotedatasource

import com.sun_asterisk.myfood.data.datasource.FoodDataSource
import com.sun_asterisk.myfood.data.remote.api.service.MyFoodApi
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.FoodResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.core.KoinComponent

class FoodRemoteDataSource(private val myFoodApi: MyFoodApi) : FoodDataSource.Remote, KoinComponent {

    override suspend fun getFoodsWithIdUser(idUser: String, page: Int) = myFoodApi.getFoodsWithIdUser(idUser, page)

    override suspend fun getFoodsOfUser(page: Int): ApiResponse<FoodResponse> = myFoodApi.getFoodsOfUser(page)

    override suspend fun createFood(
        categoryId: String,
        file: MultipartBody.Part,
        name: RequestBody,
        cost: RequestBody,
        unit: RequestBody
    ) = myFoodApi.createFood(categoryId, file, name, cost, unit)
}
