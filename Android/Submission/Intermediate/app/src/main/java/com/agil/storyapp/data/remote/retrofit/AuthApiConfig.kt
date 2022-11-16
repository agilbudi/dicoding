package com.agil.storyapp.data.remote.retrofit

import com.agil.storyapp.data.remote.response.AuthLogin
import com.agil.storyapp.data.remote.response.ResponseMessage
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiConfig {
    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<AuthLogin>


    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseMessage>
}