package com.agil.storyapp.api

import com.agil.storyapp.model.ResponseStatus
import com.agil.storyapp.model.Stories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryApi {
    @GET("stories")
    fun getAllStories(@HeaderMap authedHeaders: AuthenticatedHeaders): Call<Stories>

    @Multipart
    @POST("stories")
    fun postStory(
        @HeaderMap authedHeaders: UploadStoryHeaders,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<ResponseStatus>
}