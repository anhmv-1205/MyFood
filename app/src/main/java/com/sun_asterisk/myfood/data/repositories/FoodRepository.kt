package com.sun_asterisk.myfood.data.repositories

import com.sun_asterisk.myfood.data.datasource.FoodDataSource
import com.sun_asterisk.myfood.data.remote.remotedatasource.FoodRemoteDataSource
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.FoodResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.core.KoinComponent

class FoodRepository(private val remote: FoodRemoteDataSource) : FoodDataSource.Remote, KoinComponent {

    override suspend fun getFoodsWithIdUser(idUser: String, page: Int) =
        remote.getFoodsWithIdUser(idUser, page)

    override suspend fun getFoodsOfUser(page: Int): ApiResponse<FoodResponse> = remote.getFoodsOfUser(page)

    override suspend fun createFood(
        categoryId: String,
        file: MultipartBody.Part,
        name: RequestBody,
        cost: RequestBody,
        unit: RequestBody
    ) = remote.createFood(categoryId, file, name, cost, unit)
}
