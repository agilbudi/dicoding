package com.agil.storyapp.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseMessage(
    val error: Boolean,
    val message: String
) : Parcelable
