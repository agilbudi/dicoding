package com.abupras.eventapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abupras.eventapp.data.response.DetailEventResponse
import com.abupras.eventapp.data.response.Event
import com.abupras.eventapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
    private val _detailEvent = MutableLiveData<DetailEventResponse>()
    val detailEvent: LiveData<DetailEventResponse> = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getDetailEvent(id: Int){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetail(id)
        client.enqueue(object : Callback<DetailEventResponse>{
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _detailEvent.value = response.body()
                }else{
                    _detailEvent.value = response.body()
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                _detailEvent.value = DetailEventResponse(true,  t.message.toString(), Event("", "", 0, "", "", "", "", "", 0, "", 0, "", "", ""))
            }
        })
    }
}