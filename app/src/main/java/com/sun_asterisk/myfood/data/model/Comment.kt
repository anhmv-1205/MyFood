package com.sun_asterisk.myfood.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("_id")
    @Expose
    val id: String = "",
    @SerializedName("rating")
    @Expose
    val rating: Int = 0,
    @SerializedName("comment")
    @Expose
    val comment: String = "",
    @SerializedName("date_created")
    @Expose
    val dateCreated: String = "",
    @SerializedName("buyer")
    @Expose
    val buyer: User = User()
)
