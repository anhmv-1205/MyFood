package com.sun_asterisk.myfood.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey
    @SerializedName("_id")
    @Expose
    val id: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("phone")
    @Expose
    val phone: String,
    @SerializedName("birthday")
    @Expose
    val birthday: String,
    @SerializedName("date_created")
    @Expose
    val dateCreated: String,
    @SerializedName("authorities")
    @Expose
    val authorities: ArrayList<String>,
    @SerializedName("location")
    @Expose
    var location: ArrayList<Float>?
): Parcelable
