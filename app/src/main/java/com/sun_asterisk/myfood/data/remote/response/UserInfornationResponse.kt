package com.sun_asterisk.myfood.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInfornationResponse(
    @SerializedName("count")
    @Expose
    val count: Int = 0,
    @SerializedName("rate")
    @Expose
    val rate: Int = 0
)
