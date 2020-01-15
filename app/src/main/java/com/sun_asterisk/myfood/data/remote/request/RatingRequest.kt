package com.sun_asterisk.myfood.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RatingRequest(
    @Expose
    @SerializedName("sellerId")
    val sellerId: String,
    @Expose
    @SerializedName("buyerId")
    val buyerId: String,
    @Expose
    @SerializedName("foodId")
    val foodId: String,
    @Expose
    @SerializedName("rating")
    val rating: Int,
    @Expose
    @SerializedName("comment")
    val comment: String,
    @Expose
    @SerializedName("orderId")
    val orderId: String
)
