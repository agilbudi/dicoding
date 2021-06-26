package com.hide09.consumergithub.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetail(
    @SerializedName("login")
    val detailUserName: String?,
    @SerializedName("name")
    val detailName: String?,
    @SerializedName("avatar_url")
    val detailPhoto: String?,
    @SerializedName("public_repos")
    val detailRepo: String?,
    @SerializedName("followers")
    val detailFollowers: String?,
    @SerializedName("following")
    val detailFollowings: String?
): Parcelable{
    constructor(): this("","","","","","")
}
