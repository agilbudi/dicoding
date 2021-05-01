package com.hide09.githubapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hide09.githubapp.api.UserService
import com.hide09.githubapp.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel: ViewModel() {
    private val userService = UserService
    private val listUsers = MutableLiveData<ArrayList<User>>()

    companion object{
        private val TAG = UserViewModel::class.java.simpleName
    }

    fun setUsers() {
        userService.getUsers().enqueue( object : Callback<ArrayList<User>>{
            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                val responseUsers = response.body()
                listUsers.value = responseUsers
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    fun getUsers(): LiveData<ArrayList<User>>{
        return listUsers
    }
}