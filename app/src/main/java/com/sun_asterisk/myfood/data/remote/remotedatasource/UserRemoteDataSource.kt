package com.sun_asterisk.myfood.data.remote.remotedatasource

import com.sun_asterisk.myfood.data.datasource.UserDataSource
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.remote.api.service.MyFoodApi
import com.sun_asterisk.myfood.data.remote.request.RegisterRequest
import com.sun_asterisk.myfood.data.remote.request.SignInRequest
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import org.koin.core.KoinComponent

class UserRemoteDataSource(private val api: MyFoodApi) : UserDataSource.Remote, KoinComponent {
    override suspend fun signIn(signInRequest: SignInRequest) = api.signIn(signInRequest)

    override suspend fun getUsersWithCategoryId(categoryId: String) = api.getUsersWithCategoryId(categoryId).data!!

    override suspend fun getNumbersOfFoodByUserId(userId: String): Int =
        api.getNumbersOfFoodByUserId(userId).data ?: 0

    override suspend fun getUserByUserId(userId: String) = api.getUserByUserId(userId)

    override suspend fun register(registerRequest: RegisterRequest) = api.register(registerRequest)
}
