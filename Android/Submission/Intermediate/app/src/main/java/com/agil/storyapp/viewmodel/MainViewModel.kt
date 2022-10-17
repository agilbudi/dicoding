package com.agil.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.agil.storyapp.MainActivity
import com.agil.storyapp.api.ApiService
import com.agil.storyapp.auth.RegisterFragment
import com.agil.storyapp.model.ResponseStatus
import com.agil.storyapp.model.Stories
import com.agil.storyapp.model.Story
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MainViewModel: ViewModel() {
    private val apiService = ApiService
    private val storiesData = MutableLiveData<ArrayList<Story>>()
    private val storiesResponseStatus = MutableLiveData<ResponseStatus>()

    fun setAllStory(token: String){
        apiService.allStory(token).enqueue(object : Callback<Stories>{
            override fun onResponse(call: Call<Stories>, response: Response<Stories>) {
                val body = response.body()
                if (response.isSuccessful){
                    if (body != null) {
                        val allStories = body.listStory
                        val responseStatusStories = ResponseStatus(body.error,body.message)
                        storiesData.postValue(allStories)
                        storiesResponseStatus.postValue(responseStatusStories)
                        Log.d(MainActivity.TAG, "GET Story: ${responseStatusStories.message}")
                    }
                }else{
                    response.errorBody()?.let { getGson(it) }
                }
            }

            override fun onFailure(call: Call<Stories>, t: Throwable) {
                Log.e(MainActivity.TAG, "Response failed with "+ t.message)
            }
        })
    }


    fun getAllStory(): LiveData<ArrayList<Story>>{
        return storiesData
    }
    fun getResponseStory(): LiveData<ResponseStatus>{
        return storiesResponseStatus
    }
    private fun getGson(errorBody: ResponseBody) {
        val gson = GsonBuilder()
        try {
            val mError = gson.create().fromJson(errorBody.string(), ResponseStatus::class.java)
            storiesResponseStatus.postValue(mError)
        }catch (e: IOException){
            Log.e(RegisterFragment.TAG, "GET Story: $e")
        }
    }
}