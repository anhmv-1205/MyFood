package com.sun_asterisk.myfood.data.datasource

import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.FoodResponse
import com.sun_asterisk.myfood.utils.Constant

interface FoodDataSource {
    interface Remote {
        suspend fun getFoodsWithIdUser(idUser: String, page: Int = Constant.DEFAULT_PAGE): ApiResponse<FoodResponse>
    }
}
