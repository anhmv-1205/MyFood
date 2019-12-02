package com.sun_asterisk.myfood.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("_id")
    @Expose
    val id: String,
    @SerializedName("foodId")
    @Expose
    val foodId: String,
    @SerializedName("sellerId")
    @Expose
    val sellerId: String,
    @SerializedName("buyerId")
    @Expose
    val buyerId: String,
    @SerializedName("date_buy")
    @Expose
    val date_buy: String,
    @SerializedName("shift")
    @Expose
    val shift: String,
    @SerializedName("date_created")
    @Expose
    val dateCreated: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("note")
    @Expose
    val note: String
) : Parcelable
