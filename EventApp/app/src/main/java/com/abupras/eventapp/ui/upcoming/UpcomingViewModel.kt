package com.abupras.eventapp.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abupras.eventapp.data.response.EventResponse
import com.abupras.eventapp.data.response.ListEventsItem
import com.abupras.eventapp.data.retrofit.ApiConfig
import com.abupras.eventapp.ui.home.HomeViewModel
import com.abupras.eventapp.ui.home.HomeViewModel.Companion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {
    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        1.getNewEvent()
    }

    private fun Int.getNewEvent() {
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
        private const val TAG = "UpcomingViewModel"
    }
}