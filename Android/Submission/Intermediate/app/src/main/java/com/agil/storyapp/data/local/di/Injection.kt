package com.agil.storyapp.data.local.di

import android.content.Context
import com.agil.storyapp.data.local.room.MapStoryDatabase
import com.agil.storyapp.data.local.room.StoryDatabase
import com.agil.storyapp.data.remote.retrofit.ApiService
import com.agil.storyapp.data.repository.AddStoryRepository
import com.agil.storyapp.data.repository.AuthRepository
import com.agil.storyapp.data.repository.MapStoryRepository
import com.agil.storyapp.data.repository.StoryRepository

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository{
        val apiService = ApiService
        val database = StoryDatabase.getInstance(context)
        return StoryRepository.getInstance(apiService,database)
    }
    fun provideMapRepository(context: Context): MapStoryRepository{
        val apiService = ApiService
        val database = MapStoryDatabase.getInstance(context)
        return MapStoryRepository.getInstance(apiService, database)
    }
    fun provideAuthRepository(): AuthRepository{
        val apiService = ApiService
        return AuthRepository.getInstance(apiService)
    }
    fun provideAddStoryRepository(): AddStoryRepository{
        val apiService = ApiService
        return AddStoryRepository.getInstance(apiService)
    }
}