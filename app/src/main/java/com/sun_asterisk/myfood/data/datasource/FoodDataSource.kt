package com.sun_asterisk.myfood.data.datasource

import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.FoodResponse
import com.sun_asterisk.myfood.utils.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface FoodDataSource {
    interface Remote {
        suspend fun getFoodsWithIdUser(idUser: String, page: Int = Constant.DEFAULT_PAGE): ApiResponse<FoodResponse>

        suspend fun getFoodsOfUser(page: Int = Constant.DEFAULT_PAGE): ApiResponse<FoodResponse>

        suspend fun createFood(
            categoryId: String,
            file: MultipartBody.Part,
            name: RequestBody,
            cost: RequestBody,
            unit: RequestBody
        ): ApiResponse<Food>
    }
}
