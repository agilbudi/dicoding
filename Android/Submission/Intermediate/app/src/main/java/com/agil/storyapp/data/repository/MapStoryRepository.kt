package com.agil.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.*
import com.agil.storyapp.data.MapStoryRemoteMediator
import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.data.local.room.MapStoryDatabase
import com.agil.storyapp.data.remote.retrofit.ApiService

class MapStoryRepository private constructor(
    private val apiService: ApiService,
    private val database: MapStoryDatabase
){


    fun getListOnMapStory(token: String): LiveData<Result<PagingData<StoryEntity>>>{
        val result = MediatorLiveData<Result<PagingData<StoryEntity>>>()
        result.value = Result.Loading
        try {
            @OptIn(ExperimentalPagingApi::class)
            val data = Pager(
                config = PagingConfig(pageSize = 10),
                remoteMediator = MapStoryRemoteMediator(token, database, apiService),
                pagingSourceFactory = { database.storyDao().allStory() }
            ).liveData
            result.addSource(data){ newData ->
                result.value = Result.Success(newData)
            }
        }catch (t: Throwable){
            result.value = Result.Error(t.message.toString())
        }
        return result
    }

    fun getMapStory(): LiveData<Result<List<StoryEntity>>> {
        val result = MediatorLiveData<Result<List<StoryEntity>>>()
        result.value = Result.Loading
        try {
            val localData = database.storyDao().mapStory()
            result.addSource(localData){ newData ->
                result.value = Result.Success(newData)
            }
        }catch (t: Throwable){
            result.value = Result.Error(t.message.toString())
        }
        return result
    }

    companion object{
        val TAG: String = MapStoryRepository::class.java.simpleName
        @Volatile
        private var instance: MapStoryRepository? = null

        @JvmStatic
        fun getInstance(
            apiService: ApiService,
            database: MapStoryDatabase
        ): MapStoryRepository = instance ?: synchronized(this){
            instance ?: MapStoryRepository(apiService, database)
        }.also { instance = it }
    }
}