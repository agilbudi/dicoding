package com.hide09.githubapp.api

import android.util.Log
import com.hide09.githubapp.model.User
import com.hide09.githubapp.model.UserDetail
import com.hide09.githubapp.model.UserSearch
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserService {
    private const val BASE_URL = "https://api.github.com/"
    private val api: UserApi by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        retrofit.create(UserApi::class.java)
    }
    fun getUsers(): Call<ArrayList<User>>{
        return api.getUsers()
    }
     fun getUserDetail(username: String): Call<UserDetail>{
        return api.getDetailUsers(username)
    }
    fun getUserSearch(username: String): Call<UserSearch> {
        return api.getSearchUsers(username)
    }
    fun getUserFollowers(username: String): Call<ArrayList<User>>{
        return api.getFollowersUsers(username)
    }
    fun getUserFollowing(username: String): Call<ArrayList<User>>{
        return api.getFollowingUsers(username)
    }
}