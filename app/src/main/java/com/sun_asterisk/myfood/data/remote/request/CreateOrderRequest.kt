package com.sun_asterisk.myfood.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(
    @SerializedName("sellerId")
    @Expose
    val sellerId: String,
    @SerializedName("foodId")
    @Expose
    val foodId: String,
    @SerializedName("date_buy")
    @Expose
    val dateBuy: String,
    @SerializedName("shift")
    @Expose
    val shift: String,
    @SerializedName("note")
    @Expose
    val note: String? = null
)
