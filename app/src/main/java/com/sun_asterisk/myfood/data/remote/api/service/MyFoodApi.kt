package com.sun_asterisk.myfood.data.remote.api.service

import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.data.remote.request.SignInRequest
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.SignInResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyFoodApi {
    @POST("sign_in")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse

    // Categories
    @GET("categories")
    suspend fun getCategories(): ApiResponse<ArrayList<Category>>
}
