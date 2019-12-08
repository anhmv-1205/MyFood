package com.sun_asterisk.myfood.data.remote.api.service

import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.data.model.Order
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.remote.request.CreateOrderRequest
import com.sun_asterisk.myfood.data.remote.request.SignInRequest
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.FoodResponse
import com.sun_asterisk.myfood.data.remote.response.OrderResponse
import com.sun_asterisk.myfood.data.remote.response.SignInResponse
import com.sun_asterisk.myfood.utils.Constant
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MyFoodApi {

    // Categories
    @GET("categories")
    suspend fun getCategories(): ApiResponse<ArrayList<Category>>

    // Users
    @POST("sign_in")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse

    @GET("users_by_category_id/{categoryId}")
    suspend fun getUsersWithCategoryId(@Path("categoryId") categoryId: String): ApiResponse<ArrayList<User>>

    @GET("users/{userId}/numbers_of_foods")
    suspend fun getNumbersOfFoodByUserId(@Path("userId") userId: String): ApiResponse<Int>

    @GET("/users/{userId}")
    suspend fun getUserByUserId(@Path("userId") userId: String): ApiResponse<User>

    // Food
    @GET("foods/{userId}")
    suspend fun getFoodsWithIdUser(@Path("userId") userId: String, @Query("page") page: Int = Constant.DEFAULT_PAGE): ApiResponse<FoodResponse>

    // Order
    @POST("order")
    suspend fun createOrder(@Body createOrderRequest: CreateOrderRequest): ApiResponse<Order>

    @GET("order")
    suspend fun getOrdersOfUser(@Query("page") page: Int): ApiResponse<OrderResponse>

    @GET("order/{orderId}")
    suspend fun getOrderByOrderId(@Path("orderId") orderId: String): ApiResponse<Order>

    @PUT("order/{orderId}")
    suspend fun updateOrderStatus(@Path("orderId") orderId: String, @Query("toStatus") toStatus: String): ApiResponse<Order>
}
