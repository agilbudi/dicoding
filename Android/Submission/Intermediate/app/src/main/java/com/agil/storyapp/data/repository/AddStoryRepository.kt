package com.agil.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.agil.storyapp.data.remote.response.ResponseMessage
import com.agil.storyapp.data.remote.retrofit.ApiService
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AddStoryRepository private constructor(private val apiService: ApiService){

    private val result = MediatorLiveData<Result<ResponseMessage>>()

    fun uploadData(
        token: String, imageMultipart: MultipartBody.Part,
        description: RequestBody, lat: Float?, lon: Float?
    ): LiveData<Result<ResponseMessage>> {
        result.value = Result.Loading
        apiService.uploadStory(token, description, imageMultipart, lat, lon)
            .enqueue(object : Callback<ResponseMessage>{
            override fun onResponse(call: Call<ResponseMessage>, response: Response<ResponseMessage>) {
                if (response.isSuccessful){
                    val error = response.body()?.error as Boolean
                    val message = response.body()?.message as String
                    val addStoryResponseMessage = MutableLiveData<ResponseMessage>()
                    addStoryResponseMessage.postValue(ResponseMessage(error,message))
                    result.addSource(addStoryResponseMessage){ newData ->
                        result.value = Result.Success(newData)
                    }
                }else{
                    response.errorBody()?.let { getGson(it) }
                }
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                Log.e(TAG, "Response failed with " + t.message)
                result.value = Result.Error(t.message.toString())
            }
        })
        return result
    }

    private fun getGson(errorBody: ResponseBody) {
        val gson = GsonBuilder()
        try {
            val mError = gson.create().fromJson(errorBody.string(), ResponseMessage::class.java)
            result.value = Result.Error(mError.message)
        } catch (e: IOException) {
            Log.e(TAG, "POST Story: $e")
        }
    }

    companion object {
        val TAG : String = AddStoryRepository::class.java.simpleName
        @Volatile
        private var instance: AddStoryRepository? = null

        @JvmStatic
        fun getInstance(apiService: ApiService): AddStoryRepository= instance ?: synchronized(this){
            instance ?: AddStoryRepository(apiService)
        }.also { instance = it }
    }
}