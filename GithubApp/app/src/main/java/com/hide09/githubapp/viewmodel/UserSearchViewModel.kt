package com.hide09.githubapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hide09.githubapp.api.UserService
import com.hide09.githubapp.model.User
import com.hide09.githubapp.model.UserSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSearchViewModel: ViewModel() {
    private val userService = UserService
    private val listUsersSearch = MutableLiveData<ArrayList<User>>()

    companion object{
        private val TAG = UserSearchViewModel::class.java.simpleName
    }

    fun setUserSearch(urlusername: String?) {
        userService.getUserSearch(urlusername!!).enqueue(object : Callback<UserSearch>{
            override fun onResponse(call: Call<UserSearch>, response: Response<UserSearch>) {
                val responseUserSearch = response.body()?.items
                listUsersSearch.value = responseUserSearch
            }

            override fun onFailure(call: Call<UserSearch>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }

        })
    }
    fun getUserSearch(): LiveData<ArrayList<User>>{
        return listUsersSearch
    }
}