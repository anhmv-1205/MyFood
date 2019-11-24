package com.sun_asterisk.myfood.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.sun_asterisk.myfood.data.model.Food

data class FoodResponse(
    @SerializedName("foods")
    @Expose
    val foods: MutableList<Food>,
    @SerializedName("total_page")
    @Expose
    val totalPages: Int,
    @SerializedName("current_page")
    @Expose
    val currentPage: Int,
    @SerializedName("total_food")
    @Expose
    val totalFoods: Int
)
