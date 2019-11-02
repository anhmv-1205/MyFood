package com.sun_asterisk.myfood.data.repositories

import com.sun_asterisk.myfood.data.datasource.UserDataSource
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.remote.request.SignInRequest

class UserRepository(
    private val remote: UserDataSource.Remote,
    private val local: UserDataSource.Local
) : UserDataSource.Remote, UserDataSource.Local {

    override suspend fun getUser() = local.getUser()

    override suspend fun insertUser(user: User) = local.insertUser(user)

    override suspend fun updateUser(user: User) = local.updateUser(user)

    override suspend fun clearUser() = local.clearUser()

    override suspend fun signIn(signInRequest: SignInRequest) = remote.signIn(signInRequest)
}
