package com.sun_asterisk.myfood.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiResponse<T> {
    @Expose
    @SerializedName("status")
    val status: String? = null
    @Expose
    @SerializedName("data")
    val data: T? = null
    @SerializedName("message")
    @Expose
    val message: String? = null
}
