package com.sun_asterisk.myfood.data.remote.api.service

import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.remote.request.SignInRequest
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.SignInResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MyFoodApi {
    @POST("sign_in")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse

    // Categories
    @GET("categories")
    suspend fun getCategories(): ApiResponse<ArrayList<Category>>

    @GET("users/{categoryId}")
    suspend fun getUsersWithCategoryId(@Path("categoryId") categoryId: String): ApiResponse<ArrayList<User>>

    @GET("users/{userId}/numbers_of_foods")
    suspend fun getNumbersOfFoodByUserId(@Path("userId") userId: String): ApiResponse<Int>
}
