package com.agil.storyapp.data.remote.retrofit

import com.agil.storyapp.data.remote.response.AuthLogin
import com.agil.storyapp.data.remote.response.ResponseMessage
import com.agil.storyapp.data.remote.response.Stories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "https://story-api.dicoding.dev/v1/"
    private val authApiConfig: AuthApiConfig by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(AuthApiConfig::class.java)
    }
    private val storyApiConfig: StoryApiConfig by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(StoryApiConfig::class.java)
    }

    fun login(email: String, password: String): Call<AuthLogin> {
        return authApiConfig.postLogin(email, password)
    }

    fun register(name: String, email: String, password: String): Call<ResponseMessage> {
        return authApiConfig.postRegister(name, email, password)
    }
    suspend fun mapStory(token: String, getLocation: Int, page: Int, size: Int): Stories {
        val headers = ApiMainHeaderProvider().getNormalAuthenticatedHeaders(token)
        return storyApiConfig.getMapStories(headers, getLocation, page, size)
    }
    suspend fun pageStory(token: String, page: Int, size: Int): Stories{
        val headers = ApiMainHeaderProvider().getNormalAuthenticatedHeaders(token)
        return storyApiConfig.getPageStories(headers, page, size)
    }

    fun uploadStory(token: String, description: RequestBody, imageMultipart: MultipartBody.Part,
    lat: Float?, lon:Float?): Call<ResponseMessage> {
        val headers = ApiMainHeaderProvider().getUploadAuthenticatedHeaders(token)
        return storyApiConfig.postStory(headers, imageMultipart, description, lat, lon)
    }
}