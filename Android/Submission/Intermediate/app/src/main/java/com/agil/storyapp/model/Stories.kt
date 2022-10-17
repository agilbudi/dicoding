package com.agil.storyapp.model

data class Stories(
    val error: Boolean,
    val message: String,
    val listStory: ArrayList<Story>
)
