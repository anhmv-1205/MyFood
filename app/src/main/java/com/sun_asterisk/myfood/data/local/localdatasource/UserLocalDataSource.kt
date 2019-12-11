package com.sun_asterisk.myfood.data.local.localdatasource

import com.sun_asterisk.myfood.data.datasource.UserDataSource
import com.sun_asterisk.myfood.data.local.dao.UserDao
import com.sun_asterisk.myfood.data.local.sharedprf.SharedPrefApi
import com.sun_asterisk.myfood.data.model.User
import org.koin.core.KoinComponent

class UserLocalDataSource(
    private val sharedPrefApi: SharedPrefApi,
    private val userDao: UserDao
) : UserDataSource.Local, KoinComponent {

    override fun getUser() = userDao.getUser()

    override suspend fun getRoleOfUser() = userDao.getRole()

    override suspend fun insertUser(user: User) = userDao.insertUser(user)

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    override suspend fun clearUser() {
        userDao.clearUser()
    }
}
