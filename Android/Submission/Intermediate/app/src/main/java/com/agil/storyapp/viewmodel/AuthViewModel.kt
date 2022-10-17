package com.agil.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agil.storyapp.api.ApiService
import com.agil.storyapp.auth.LoginFragment
import com.agil.storyapp.auth.RegisterFragment
import com.agil.storyapp.model.AuthLogin
import com.agil.storyapp.model.ResponseStatus
import com.agil.storyapp.model.User
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AuthViewModel: ViewModel() {
    private val authService = ApiService
    private var myEmail = String()
    private val loginResponseStatus = MutableLiveData<ResponseStatus?>()
    private val registerResponseStatus = MutableLiveData<ResponseStatus?>()
    private val userData = MutableLiveData<User>()

    fun setEmail(email: String) {
        myEmail = email
    }
    fun registerData(name: String, email: String, password: String){
        authService.register(name, email, password).enqueue(object : Callback<ResponseStatus>{
            override fun onResponse(call: Call<ResponseStatus>, response: Response<ResponseStatus>) {
                val body = response.body()
                val message = body?.message
                if (response.isSuccessful){
                    if (body != null) {
                        registerResponseStatus.postValue(ResponseStatus(body.error, body.message))
                        Log.i(RegisterFragment.TAG, "Register: $message")
                    }
                }else{
                    val gson = GsonBuilder()
                    try {
                        val mError = gson.create().fromJson(response.errorBody()?.string(), ResponseStatus::class.java)
                        registerResponseStatus.postValue(mError)
                    }catch (e: IOException){
                        Log.e(RegisterFragment.TAG, "Register: $e")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseStatus>, t: Throwable) {
                Log.e(RegisterFragment.TAG,"Response Failed with: "+ t.message)
            }
        })
    }
    fun loginData(email: String, password: String){
        authService.login(email, password).enqueue(object : Callback<AuthLogin>{
            override fun onResponse(call: Call<AuthLogin>, response: Response<AuthLogin>) {
                val body = response.body()
                val message = body?.message
                if (response.isSuccessful){
                    if (body != null) {
                        val user = body.loginResult
                        val toResponseStatus = ResponseStatus(body.error, body.message)
                        userData.postValue(user)
                        loginResponseStatus.postValue(toResponseStatus)
                        Log.i(LoginFragment.TAG, "Login: $message")
                    }
                }else{
                    val gson = GsonBuilder()
                    try {
                        val mError = gson.create().fromJson(response.errorBody()?.string(), ResponseStatus::class.java)
                        loginResponseStatus.postValue(mError)
                    }catch (e: IOException){
                        Log.e(RegisterFragment.TAG, "Login: $e")
                    }
                }
            }

            override fun onFailure(call: Call<AuthLogin>, t: Throwable) {
                Log.e(LoginFragment.TAG, "Response Failed with: "+ t.message)
            }
        })
    }

    fun resetResponseStatus() {
        loginResponseStatus.postValue(null)
        registerResponseStatus.postValue(null)
    }
    fun getLoginResponse(): LiveData<ResponseStatus?> {
        return loginResponseStatus
    }
    fun getRegisterResponse(): LiveData<ResponseStatus?> {
        return registerResponseStatus
    }
    fun getUserData(): LiveData<User>{
        return userData
    }
    fun getEmail(): String{
        return myEmail
    }
}