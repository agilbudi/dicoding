package com.hide09.githubapp.api

import com.hide09.githubapp.BuildConfig
import com.hide09.githubapp.model.User
import com.hide09.githubapp.model.UserDetail
import com.hide09.githubapp.model.UserSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    companion object{
        private const val key = BuildConfig.GITHUB_TOKEN
    }

    @GET("users")
    @Headers("Authorization: token $key")
    fun getUsers(): Call<ArrayList<User>>

    @GET("search/users")
    @Headers("Authorization: token $key")
    fun getSearchUsers(@Query("q") username: String): Call<UserSearch>

    @GET("users/{username}")
    @Headers("Authorization: token $key")
    fun getDetailUsers(@Path("username") username: String): Call<UserDetail>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $key")
    fun getFollowersUsers(@Path("username") username: String): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $key")
    fun getFollowingUsers(@Path("username") username: String): Call<ArrayList<User>>
}