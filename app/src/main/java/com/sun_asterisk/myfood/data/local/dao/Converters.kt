package com.sun_asterisk.myfood.data.local.dao

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sun_asterisk.myfood.utils.extension.notNull

class Converters {

    @TypeConverter
    fun fromStringToArrayList(value: String): ArrayList<String>? {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        value.notNull {
            return Gson().fromJson(it, listType)
        }
        return null
    }

    @TypeConverter
    fun fromArrayListToString(list: ArrayList<String>?): String? {
        list.notNull {
            return Gson().toJson(list)
        }
        return null
    }

    @TypeConverter
    fun fromJsonToLocation(value: String): ArrayList<Float>? {
        val listType = object : TypeToken<ArrayList<Float>>() {}.type
        value.notNull {
            return Gson().fromJson(it, listType)
        }
        return null
    }

    @TypeConverter
    fun fromArrayListFloatToString(list: ArrayList<Float>?): String? {
        list.notNull {
            return Gson().toJson(list)
        }
        return null
    }
}
