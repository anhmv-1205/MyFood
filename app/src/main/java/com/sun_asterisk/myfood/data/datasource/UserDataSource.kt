package com.sun_asterisk.myfood.data.datasource

import androidx.lifecycle.LiveData
import com.sun_asterisk.myfood.data.model.User
import com.sun_asterisk.myfood.data.remote.request.RegisterRequest
import com.sun_asterisk.myfood.data.remote.request.SignInRequest
import com.sun_asterisk.myfood.data.remote.response.ApiResponse
import com.sun_asterisk.myfood.data.remote.response.SignInResponse

interface UserDataSource {
    interface Local {
        fun getUser(): LiveData<User>

        suspend fun getRoleOfUser(): Int

        suspend fun insertUser(user: User): Long

        suspend fun updateUser(user: User)

        suspend fun clearUser()
    }

    interface Remote {
        suspend fun signIn(signInRequest: SignInRequest): SignInResponse

        suspend fun getUsersWithCategoryId(categoryId: String): ArrayList<User>

        suspend fun getNumbersOfFoodByUserId(userId: String): Int

        suspend fun getUserByUserId(userId: String): ApiResponse<User>

        suspend fun register(registerRequest: RegisterRequest): ApiResponse<User>
    }
}
