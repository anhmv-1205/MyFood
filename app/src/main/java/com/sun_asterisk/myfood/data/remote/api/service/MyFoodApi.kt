package com.sun_asterisk.myfood.data.remote.api.service

import com.sun_asterisk.myfood.data.model.Category
import com.sun_asterisk.myfood.data.model.Comment
import com.sun_asterisk.myfood.data.model.Food
import com.sun_asterisk.myfood.data.model.Order
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.remote.request.CreateOrderRequest
import com.sun_asterisk.myfood.data.remote.request.RatingRequest
import com.sun_asterisk.myfood.data.remote.request.RegisterRequest
import com.sun_asterisk.myfood.data.remote.request.SignInRequest
import com.sun_asterisk.myfood.data.remote.request.UpdateFoodRequest
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.FoodResponse
import com.sun_asterisk.myfood.data.remote.response.OrderResponse
import com.sun_asterisk.myfood.data.remote.response.SignInResponse
import com.sun_asterisk.myfood.data.remote.response.UserInfornationResponse
import com.sun_asterisk.myfood.utils.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<User>

    @GET("users/information/{userId}")
    suspend fun getUserInformationRelatedFood(@Path("userId") userId: String): ApiResponse<UserInfornationResponse>

    // Food
    @GET("foods/{userId}")
    suspend fun getFoodsWithIdUser(@Path("userId") userId: String, @Query("page") page: Int = Constant.DEFAULT_PAGE): ApiResponse<FoodResponse>

    @GET("foods")
    suspend fun getFoodsOfUser(@Query("page") page: Int): ApiResponse<FoodResponse>

    @Multipart
    @POST("foods/{categoryId}")
    suspend fun createFood(
        @Path("categoryId") categoryId: String,
        @Part file: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("cost") cost: RequestBody,
        @Part("unit") unit: RequestBody
    ): ApiResponse<Food>

    @DELETE("/foods/{foodId}")
    suspend fun deleteFoodById(@Path("foodId") foodId: String): ApiResponse<Any>

    @PUT("/foods/{foodId}")
    suspend fun updateFood(@Path("foodId") foodId: String, @Body updateFoodRequest: UpdateFoodRequest): ApiResponse<Food>

    @Multipart
    @PUT("/foods/{foodId}")
    suspend fun editFood(
        @Path("foodId") foodId: String,
        @Part("categoryId") categoryId: RequestBody,
        @Part file: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("cost") cost: RequestBody,
        @Part("unit") unit: RequestBody
    ): ApiResponse<Food>

    // Order
    @POST("order")
    suspend fun createOrder(@Body createOrderRequest: CreateOrderRequest): ApiResponse<Order>

    @GET("order")
    suspend fun getOrdersOfUser(@Query("page") page: Int): ApiResponse<OrderResponse>

    @GET("order/{orderId}")
    suspend fun getOrderByOrderId(@Path("orderId") orderId: String): ApiResponse<Order>

    @PUT("order/{orderId}")
    suspend fun updateOrderStatus(@Path("orderId") orderId: String, @Query("toStatus") toStatus: String): ApiResponse<Order>

    // Comment
    @POST("comment")
    suspend fun createComment(@Body ratingRequest: RatingRequest): ApiResponse<Any>

    @GET("comment/{farmerId}")
    suspend fun getCommentsByFarmerId(@Path("farmerId") farmerId: String): ApiResponse<ArrayList<Comment>>
}
