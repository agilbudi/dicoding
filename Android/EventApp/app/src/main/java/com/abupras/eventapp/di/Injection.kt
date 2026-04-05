package com.abupras.eventapp.di

import android.content.Context
import com.abupras.eventapp.data.EventRepository
import com.abupras.eventapp.data.local.room.FavoriteEventRoomDB
import com.abupras.eventapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository{
        val apiService = ApiConfig.getApiService()
        val database = FavoriteEventRoomDB.getInstance(context)
        val dao = database.eventDao()
        return EventRepository.getInstance(apiService, dao)

    }
}