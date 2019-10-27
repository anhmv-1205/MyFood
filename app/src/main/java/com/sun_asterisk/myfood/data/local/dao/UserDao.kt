package com.sun_asterisk.myfood.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sun_asterisk.myfood.data.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM user")
    suspend fun clearUser()
}
