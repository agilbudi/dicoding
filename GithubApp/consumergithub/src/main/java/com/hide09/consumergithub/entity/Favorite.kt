package com.hide09.consumergithub.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    val username: String?,
    val photo: String?
): Parcelable {
    constructor(): this("","")
}