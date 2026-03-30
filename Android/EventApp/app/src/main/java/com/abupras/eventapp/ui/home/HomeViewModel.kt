package com.abupras.eventapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abupras.eventapp.data.response.EventResponse
import com.abupras.eventapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listEvents = MutableLiveData<EventResponse?>()
    val listEvents: LiveData<EventResponse?> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _title = MutableLiveData<String>()
    val title : LiveData<String> = _title

    init {
        getAllEvents()
        _title.value = "All Events Here"
    }
    fun getAllEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    _listEvents.value = response.body()
                }else{
                    _listEvents.value = response.body()
                    Log.e(TAG, "onRequest: ${responseBody?.message}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _listEvents.value = EventResponse(emptyList(),true, t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }

    fun getSearchEvent(active: Int, query: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearch(active, query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful) {
                    _listEvents.value = response.body()
                }else{
                    _listEvents.value = response.body()
                    Log.e(TAG, "onRequest: ${responseBody?.message}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                _listEvents.value = EventResponse(emptyList(),true, t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "HomeViewModel"

    }
}