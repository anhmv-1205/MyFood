package com.sun_asterisk.myfood.data.repositories

import com.sun_asterisk.myfood.data.local.sharedprf.SharedPrefApi
import com.sun_asterisk.myfood.data.local.sharedprf.SharedPrefsKey
import com.sun_asterisk.myfood.utils.extension.notNull

class TokenRepository(private val sharedPrefApi: SharedPrefApi) {

    private var token: String? = null

    fun getToken(): String? {
        token.notNull { return it }

        sharedPrefApi.get(SharedPrefsKey.KEY_TOKEN, String::class.java).notNull {
            token = it
            return it
        }

        return null
    }

    fun saveToken(token: String) {
        this.token = token
        sharedPrefApi.put(SharedPrefsKey.KEY_TOKEN, token)
    }

    fun clearToken() {
        token = null
        sharedPrefApi.clearToken()
    }
}
