package com.agil.storyapp.api

import com.agil.storyapp.model.AuthLogin
import com.agil.storyapp.model.ResponseStatus
import retrofit2.Call
import retrofit2.http.*

interface AuthApi {
    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email:String,
        @Field("password") password: String
    ): Call<AuthLogin>


    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseStatus>
}