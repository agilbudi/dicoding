package com.hide09.githubapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class User(
        @SerializedName("login")
    val username: String?,
        @SerializedName("avatar_url")
    val foto: String?
) : Parcelable{
    constructor(): this("","")
}