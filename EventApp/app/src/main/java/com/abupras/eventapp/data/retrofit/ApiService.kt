package com.abupras.eventapp.data.retrofit

import com.abupras.eventapp.data.response.ListEventsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events/{active}")
    fun getEvents(@Path("active") active: Int? = null): Call<List<ListEventsItem>>

    @GET("events/:{id}")
    fun getDetail(@Path("id") id: Int): Call<ListEventsItem>
}