package com.agil.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.agil.storyapp.data.remote.response.AuthLogin
import com.agil.storyapp.data.remote.response.ResponseMessage
import com.agil.storyapp.data.remote.retrofit.ApiService
import com.agil.storyapp.model.User
import com.agil.storyapp.ui.auth.RegisterFragment
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AuthRepository(private val apiService: ApiService) {
    private val result = MediatorLiveData<Result<ResponseMessage>>()
    private val resultLogin = MediatorLiveData<Result<AuthLogin>>()
    private var myEmail: String = ""

    fun register(name: String, email: String, password: String): LiveData<Result<ResponseMessage>> {
        result.value = Result.Loading
        apiService.register(name, email, password).enqueue(object : Callback<ResponseMessage>{
            override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                if (response.isSuccessful){
                    val error = response.body()?.error as Boolean
                    val message = response.body()?.message as String
                    val responseRegister = MutableLiveData<ResponseMessage>()
                    responseRegister.postValue(ResponseMessage(error, message))
                    result.addSource(responseRegister){ newData ->
                        result.value = Result.Success(newData)
                    }
                }else{
                    val errorMessage = getGson(response.errorBody())
                    result.value = Result.Error(errorMessage)
                }
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    fun login(email: String, password: String): LiveData<Result<AuthLogin>>{
        resultLogin.value = Result.Loading
        apiService.login(email, password).enqueue(object : Callback<AuthLogin>{
            override fun onResponse(call: Call<AuthLogin>, response: Response<AuthLogin>) {
                if (response.isSuccessful){
                    val error = response.body()?.error as Boolean
                    val message = response.body()?.message as String
                    val user = response.body()?.loginResult as User
                    val responseData = MutableLiveData<AuthLogin>()
                    responseData.postValue(AuthLogin(error,message,user))
                    resultLogin.addSource(responseData){ newData ->
                        resultLogin.value = Result.Success(newData)
                    }
                }else{
                    val errorMessage = getGson(response.errorBody())
                    resultLogin.value = Result.Error(errorMessage)
                }
            }

            override fun onFailure(call: Call<AuthLogin>, t: Throwable) {
                resultLogin.value = Result.Error(t.message.toString())
            }
        })
        return resultLogin
    }

    fun setEmail(email: String?) {
        if (email != null){
            myEmail = email
        }else {
            throw IllegalArgumentException("data empty")
        }
    }
    fun getEmail(): String { return myEmail }

    private fun getGson(errorBody: ResponseBody?): String {
        val gson = GsonBuilder()
        var errorMessage= ""
        try {
            val mError = gson.create().fromJson(errorBody?.string(), ResponseMessage::class.java)
            errorMessage = mError.message
        } catch (e: IOException) {
            Log.e(RegisterFragment.TAG, "POST Story: $e")
        }
        return errorMessage
    }

    companion object{
        @Volatile
        private var instance: AuthRepository? = null

        @JvmStatic
        fun getInstance(apiService: ApiService): AuthRepository = instance ?: synchronized(this){
            instance ?: AuthRepository(apiService)
        }.also { instance = it }
    }
}