package com.hide09.githubapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hide09.githubapp.api.UserService
import com.hide09.githubapp.model.UserDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel: ViewModel() {
    private val userService = UserService
    private val userDetail = MutableLiveData<UserDetail>()

    companion object{
        private val TAG = UserDetailViewModel::class.java.simpleName
    }

    fun setDataUserDetail(username: String?){
        userService.getUserDetail(username!!).enqueue(object : Callback<UserDetail>{
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                if (response.isSuccessful){
                    val responseUsers = response.body()
                    userDetail.value = responseUsers
                }
            }
            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }
    fun getUserDetail(): LiveData<UserDetail>{
        return userDetail
    }

}