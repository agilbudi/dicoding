package com.hide09.githubapp.api

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
        private const val token1 = "Authorization: token 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"
    }

    @GET("users")
    @Headers(token1)
    fun getUsers(): Call<ArrayList<User>>

    @GET("search/users")
    @Headers(token1)
    fun getSearchUsers(@Query("q") username: String): Call<UserSearch>

    @GET("users/{username}")
    @Headers(token1)
    fun getDetailUsers(@Path("username") username: String): Call<UserDetail>

    @GET("users/{username}/followers")
    @Headers(token1)
    fun getFollowersUsers(@Path("username") username: String): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers(token1)
    fun getFollowingUsers(@Path("username") username: String): Call<ArrayList<User>>
}