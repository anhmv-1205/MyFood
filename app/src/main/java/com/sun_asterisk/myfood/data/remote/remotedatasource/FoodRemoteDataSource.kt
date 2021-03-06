package com.sun_asterisk.myfood.data.remote.remotedatasource

import com.sun_asterisk.myfood.data.datasource.FoodDataSource
import com.sun_asterisk.myfood.data.remote.api.service.MyFoodApi
import com.sun_asterisk.myfood.data.remote.request.UpdateFoodRequest
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.FoodResponse
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part
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

    override suspend fun deleteFoodById(foodId: String) = myFoodApi.deleteFoodById(foodId)

    override suspend fun updateFood(
        foodId: String,
        updateFoodRequest: UpdateFoodRequest
    ) = myFoodApi.updateFood(foodId, updateFoodRequest)

    override suspend fun editFood(
        foodId: String,
        categoryId: RequestBody,
        file: Part?,
        name: RequestBody,
        cost: RequestBody,
        unit: RequestBody
    ) = myFoodApi.editFood(foodId, categoryId, file, name, cost, unit)
}
