package com.sun_asterisk.myfood.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.sun_asterisk.myfood.data.model.User

data class SignInResponse(
    @SerializedName("token")
    @Expose
    val token: String,
    @SerializedName("data")
    @Expose
    val user: User,
    @SerializedName("message")
    @Expose
    val message: String,
    @Expose
    @SerializedName("status")
    val status: String
)
