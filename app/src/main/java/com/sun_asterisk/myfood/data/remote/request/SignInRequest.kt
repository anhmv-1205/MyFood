package com.sun_asterisk.myfood.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("password")
    @Expose
    val password: String
)
