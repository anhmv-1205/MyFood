package com.sun_asterisk.myfood.data.repositories

import com.sun_asterisk.myfood.data.datasource.UserDataSource
import com.sun_asterisk.myfood.data.local.localdatasource.UserLocalDataSource
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.remote.remotedatasource.UserRemoteDataSource
import com.sun_asterisk.myfood.data.remote.request.SignInRequest
import org.koin.core.KoinComponent

class UserRepository(
    private val remote: UserRemoteDataSource,
    private val local: UserLocalDataSource
) : UserDataSource.Remote, UserDataSource.Local, KoinComponent {

    override fun getUser() = local.getUser()

    override suspend fun insertUser(user: User) = local.insertUser(user)

    override suspend fun updateUser(user: User) = local.updateUser(user)

    override suspend fun clearUser() = local.clearUser()

    override suspend fun signIn(signInRequest: SignInRequest) = remote.signIn(signInRequest)

    override suspend fun getUsersWithCategoryId(categoryId: String) = remote.getUsersWithCategoryId(categoryId)

    override suspend fun getNumbersOfFoodByUserId(userId: String) = remote.getNumbersOfFoodByUserId(userId)
}
