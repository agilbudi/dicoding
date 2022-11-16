package com.agil.storyapp.data.remote.retrofit

import com.agil.storyapp.data.remote.response.ResponseMessage
import com.agil.storyapp.data.remote.response.Stories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryApiConfig {
    @GET("stories")
    suspend fun getMapStories(
        @HeaderMap authedHeaders: AuthenticatedHeaders,
        @Query("location") location: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Stories
    @GET("stories")
    fun getMapStoriesNoPaging(
        @HeaderMap authedHeaders: AuthenticatedHeaders,
        @Query("location") location: Int
    ): Call<Stories>

    @Multipart
    @POST("stories")
    fun postStory(
        @HeaderMap authedHeaders: UploadStoryHeaders,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ): Call<ResponseMessage>

    @GET("stories")
    suspend fun getPageStories(
        @HeaderMap authedHeaders: AuthenticatedHeaders,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Stories
}