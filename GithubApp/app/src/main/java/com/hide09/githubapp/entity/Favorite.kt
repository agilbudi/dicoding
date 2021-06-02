package com.hide09.githubapp.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    var id: Int = 0,
    var username: String? = null,
    var name: String? = null,
    var photo: String? = null,
    var repo: String? = null,
    var followers: String? = null,
    var followings: String? = null
): Parcelable
