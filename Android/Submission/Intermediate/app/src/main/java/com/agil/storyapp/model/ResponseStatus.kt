package com.agil.storyapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseStatus(
    val error: Boolean,
    val message: String
): Parcelable
