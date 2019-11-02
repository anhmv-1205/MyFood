package com.sun_asterisk.myfood.data.local.sharedprf

interface SharedPrefApi {
    fun <T> get(key: String, clazz: Class<T>): T

    fun <T> put(key: String, data: T)

    fun clearToken()

    fun clear()
}
