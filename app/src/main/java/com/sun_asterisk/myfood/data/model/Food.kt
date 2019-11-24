package com.sun_asterisk.myfood.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Food(
    @SerializedName("_id")
    @Expose
    val id: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("img_url")
    @Expose
    val imgUrl: String = "",
    @SerializedName("userId")
    @Expose
    val farmerId: String = "",
    @SerializedName("categoryId")
    @Expose
    val categoryId: String = "",
    @SerializedName("cost")
    @Expose
    val cost: Int = 0,
    @SerializedName("unit_cost")
    @Expose
    val unitCost: String? = "",
    @SerializedName("unit")
    @Expose
    val unitFood: String = "",
    @SerializedName("amount_buy")
    @Expose
    val amountBuy: Int = 0,
    @SerializedName("state")
    @Expose
    val state: Boolean = true,
    @SerializedName("date_created")
    @Expose
    val dateCreated: String = ""
)
