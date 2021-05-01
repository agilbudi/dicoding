package com.hide09.githubapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetail(
    @SerializedName("login")
    val detailUserName: String?,
    @SerializedName("name")
    val detailNama: String?,
    @SerializedName("avatar_url")
    val detailFoto: String?,
    @SerializedName("public_repos")
    val detailRepo: String?,
    @SerializedName("followers")
    val detailFollowers: String?,
    @SerializedName("following")
    val detailFollowing: String?
): Parcelable{
    constructor(): this("","","","","","")
}
