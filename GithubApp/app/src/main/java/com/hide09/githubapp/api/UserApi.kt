package com.hide09.githubapp.api

import com.hide09.githubapp.model.User
import com.hide09.githubapp.model.UserDetail
import com.hide09.githubapp.model.UserSearch
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    @GET("users")
    @Headers("Authorization: token <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>")
    fun getUsers(): Call<ArrayList<User>>

    @GET("search/users")
    @Headers("Authorization: token <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>")
    fun getSearchUsers(@Query("q") username: String): Call<UserSearch>

    @GET("users/{username}")
    @Headers("Authorization: token <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>")
    fun getDetailUsers(@Path("username") username: String): Call<UserDetail>

    @GET("users/{username}/followers")
    @Headers("Authorization: token <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>")
    fun getFollowersUsers(@Query("username") username: String): Call<User>

    @GET("users/{username}/following")
    @Headers("Authorization: token <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>")
    fun getFollowingUsers(@Query("username") username: String): Call<User>
}