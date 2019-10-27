package com.sun_asterisk.myfood.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sun_asterisk.myfood.data.local.dao.Converters
import com.sun_asterisk.myfood.data.local.dao.UserDao
import com.sun_asterisk.myfood.data.model.User

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}
