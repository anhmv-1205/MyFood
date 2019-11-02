package com.sun_asterisk.myfood.data.remote.remotedatasource

import com.sun_asterisk.myfood.data.datasource.UserDataSource
import com.sun_asterisk.myfood.data.remote.api.service.MyFoodApi
import com.sun_asterisk.myfood.data.remote.request.SignInRequest
import org.koin.core.KoinComponent

class UserRemoteDataSource(private val api: MyFoodApi) : UserDataSource.Remote, KoinComponent {
    override suspend fun signIn(signInRequest: SignInRequest) = api.signIn(signInRequest)
}