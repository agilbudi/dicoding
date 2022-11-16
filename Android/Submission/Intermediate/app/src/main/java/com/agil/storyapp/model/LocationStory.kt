package com.agil.storyapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationStory(
    val id: String,
    val name: String,
    val desc: String,
    val photoUrl: String,
    val latitude: Float,
    val longitude: Float
): Parcelable
