package com.hide09.consumergithub.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hide09.consumergithub.api.UserService
import com.hide09.consumergithub.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailTabViewModel: ViewModel() {
    private val userService = UserService
    private val userFollowerList = MutableLiveData<ArrayList<User>>()
    private val userFollowingList = MutableLiveData<ArrayList<User>>()

    companion object{
        private val TAG = UserDetailTabViewModel::class.java.simpleName
    }

    fun setUserFollowing(username: String){
        userService.getUserFollowing(username).enqueue(object : Callback<ArrayList<User>>{
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                val responseUsers = response.body()
                userFollowingList.postValue(responseUsers)
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }
    fun setUserFollowers(username: String){
        userService.getUserFollowers(username).enqueue(object : Callback<ArrayList<User>>{
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                val responseUsers = response.body()
                userFollowerList.postValue(responseUsers)
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun getUserFollowing(): LiveData<ArrayList<User>>{
        return userFollowingList
    }
    fun getUserFollowers(): LiveData<ArrayList<User>>{
        return userFollowerList
    }
}