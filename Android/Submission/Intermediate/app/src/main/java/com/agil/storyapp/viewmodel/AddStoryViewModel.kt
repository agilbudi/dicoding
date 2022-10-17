package com.agil.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agil.storyapp.AddStoryActivity
import com.agil.storyapp.api.ApiService
import com.agil.storyapp.auth.RegisterFragment
import com.agil.storyapp.model.ResponseStatus
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class AddStoryViewModel: ViewModel() {
    private val apiService = ApiService
    private val addStoryResponseStatus = MutableLiveData<ResponseStatus>()
    private var imageFile : File? = null
    private var textDesc = String()

    fun postStory(token: String, imageMultipart: MultipartBody.Part, description: RequestBody) {
        apiService.uploadStory(token,description,imageMultipart).enqueue(object : Callback<ResponseStatus>{
            override fun onResponse(call: Call<ResponseStatus>, response: Response<ResponseStatus>) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error){
                        addStoryResponseStatus.postValue(ResponseStatus(responseBody.error,responseBody.message))
                        Log.d(AddStoryActivity.TAG, "OnSuccessful - "+responseBody.message)
                    }
                }else{
                    response.errorBody()?.let { getGson(it) }
                }
            }
            override fun onFailure(call: Call<ResponseStatus>, t: Throwable) {
            }
        })
    }
    fun setImageFile(url: File){
        imageFile = url
    }
    fun setDesc(desc: String){
        textDesc = desc
    }

    fun getUrl(): File? {
        return imageFile
    }
    fun getDesc():String{
        return textDesc
    }
    fun getResponseStatus(): LiveData<ResponseStatus>{
        return addStoryResponseStatus
    }

    private fun getGson(errorBody: ResponseBody) {
        val gson = GsonBuilder()
        try {
            val mError = gson.create().fromJson(errorBody.string(), ResponseStatus::class.java)
            addStoryResponseStatus.postValue(mError)
            Log.d(AddStoryActivity.TAG, "OnUnsuccessful - "+mError.message)
        }catch (e: IOException){
            Log.e(RegisterFragment.TAG, "POST StoryData: $e")
        }
    }
}