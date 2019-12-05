package com.sun_asterisk.myfood.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.sun_asterisk.myfood.data.model.Order

data class OrderResponse(
    @SerializedName("orders")
    @Expose
    val orders: MutableList<Order>,
    @SerializedName("total_page")
    @Expose
    val totalPages: Int,
    @SerializedName("current_page")
    @Expose
    val currentPage: Int,
    @SerializedName("total_item")
    @Expose
    val totalFoods: Int
)
