package com.sun_asterisk.myfood.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("password")
    @Expose
    val password: String,
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("birthday")
    @Expose
    val birthday: String,
    @SerializedName("role")
    @Expose
    val role: Int,
    @SerializedName("location")
    @Expose
    val location: ArrayList<Float>? = null
)
