package com.agil.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.*
import com.agil.storyapp.data.StoryRemoteMediator
import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.data.local.room.StoryDatabase
import com.agil.storyapp.data.remote.retrofit.ApiService

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val database: StoryDatabase
){
    private val result = MediatorLiveData<Result<PagingData<StoryEntity>>>()

    @OptIn(ExperimentalPagingApi::class)
    fun getPageStory(token: String): LiveData<Result<PagingData<StoryEntity>>> {
        try {
            result.value = Result.Loading
            val data = Pager(
                config = PagingConfig(pageSize = 5),
                remoteMediator = StoryRemoteMediator(token,database, apiService),
                pagingSourceFactory = {database.storyDao().allStory()}
            ).liveData
            result.addSource(data){ newData ->
                result.value = Result.Success(newData)
            }
        }catch(t:Throwable){
            result.value = Result.Error(t.message.toString())
        }

        return result
    }

    companion object{
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: ApiService,
            database: StoryDatabase,
        ): StoryRepository = instance?: synchronized(this){
            instance?: StoryRepository(apiService, database)
        }.also { instance = it }
    }
}