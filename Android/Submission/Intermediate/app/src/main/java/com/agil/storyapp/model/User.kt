package com.agil.storyapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var userId: String,
    var name: String,
    var token: String,
    var lat: Float?,
    var lon: Float?
) : Parcelable {
    constructor() : this("", "", "",null,null)
}
