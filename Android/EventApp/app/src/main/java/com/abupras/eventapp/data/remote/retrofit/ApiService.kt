package com.abupras.eventapp.data.remote.retrofit

import com.abupras.eventapp.data.remote.response.DetailEventResponse
import com.abupras.eventapp.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    suspend fun getAllEvents(): EventResponse

    @GET("events")
    suspend fun getActiveEvents(@Query("active") active: Int): EventResponse

    @GET("events/{id}")
    suspend fun getDetail(@Path("id") id: Int): DetailEventResponse

    @GET("events")
    suspend fun getSearch(
        @Query("active") active: Int,
        @Query("q")query: String): EventResponse
}