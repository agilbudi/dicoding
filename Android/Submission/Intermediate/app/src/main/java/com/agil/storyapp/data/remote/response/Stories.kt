package com.agil.storyapp.data.remote.response

import com.agil.storyapp.model.Story

data class Stories(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>
)
