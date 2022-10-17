package com.agil.storyapp.api

import com.agil.storyapp.model.AuthLogin
import com.agil.storyapp.model.ResponseStatus
import com.agil.storyapp.model.Stories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "https://story-api.dicoding.dev/v1/"
    private val authApi: AuthApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(AuthApi::class.java)
    }
    private val storyApi: StoryApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(StoryApi::class.java)
    }

    fun login(email: String, password: String):Call<AuthLogin>{
        return authApi.postLogin(email, password)
    }
    fun register(name: String, email: String, password: String): Call<ResponseStatus>{
        return authApi.postRegister(name, email, password)
    }
    fun allStory(token: String): Call<Stories>{
        val headers = ApiMainHeaderProvider().getAuthenticatedHeaders(token)
        return storyApi.getAllStories(headers)
    }
    fun uploadStory(token: String, description: RequestBody, imageMultipart: MultipartBody.Part):Call<ResponseStatus>{
        val headers = ApiMainHeaderProvider().getUploadAuthenticatedHeaders(token)
        return storyApi.postStory(headers,imageMultipart,description)
    }
}