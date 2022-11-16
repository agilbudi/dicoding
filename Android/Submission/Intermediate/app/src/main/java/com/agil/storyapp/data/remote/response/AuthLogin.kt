package com.agil.storyapp.data.remote.response

import com.agil.storyapp.model.User

data class AuthLogin(
    val error: Boolean,
    val message: String,
    val loginResult: User
)
