package com.agil.storyapp.model

data class AuthLogin(
    val error: Boolean,
    val message: String,
    val loginResult: User
)
