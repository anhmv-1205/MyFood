package com.sun_asterisk.myfood.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateFoodRequest(
    @SerializedName("state")
    @Expose
    val state: Boolean? = null,
    @SerializedName("name")
    @Expose
    val name: String? = null,
    @SerializedName("cost")
    @Expose
    val cost: Int? = null,
    @SerializedName("unit")
    @Expose
    val unit: String? = null,
    @SerializedName("categoryId")
    @Expose
    val categoryId: String? = null
)
