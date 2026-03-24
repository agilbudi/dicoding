package com.abupras.eventapp.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abupras.eventapp.data.response.EventResponse
import com.abupras.eventapp.data.response.ListEventsItem
import com.abupras.eventapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {
    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _title = MutableLiveData<String>()
    val title : LiveData<String> = _title

    init {
        0.getFinishedEvent()
        _title.value = "Events Finished"
    }

    private fun Int.getFinishedEvent(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(this)
        client.enqueue(object : Callback<EventResponse>{
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful){
                    if (responseBody != null && !responseBody.error){
                        _listEvents.value = responseBody.listEvents
                    }
                }else{
                    Log.e(TAG, "onFailure: ${responseBody?.message}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    companion object{
        private const val TAG = "FinishedViewModel"
    }
}