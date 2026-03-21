package com.abupras.eventapp.data.retrofit

import com.abupras.eventapp.data.response.DetailEventResponse
import com.abupras.eventapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getAllEvents(): Call<EventResponse>

    @GET("events")
    fun getEvents(@Query("active") active: Int): Call<EventResponse>

    @GET("events/:{id}")
    fun getDetail(@Path("id") id: Int): Call<DetailEventResponse>
}