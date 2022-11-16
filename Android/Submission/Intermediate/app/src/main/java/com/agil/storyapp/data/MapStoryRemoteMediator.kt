package com.agil.storyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.agil.storyapp.data.local.entity.RemoteKeys
import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.data.local.room.MapStoryDatabase
import com.agil.storyapp.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class MapStoryRemoteMediator(
    private val token: String,
    private val database: MapStoryDatabase,
    private val apiService: ApiService
): RemoteMediator<Int, StoryEntity>(){

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND ->{
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey =
                    remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey

            }
            LoadType.APPEND ->{
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey =
                    remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.mapStory(token, getLocation = 1, page,state.config.pageSize).listStory
            val data = responseData.map {
                StoryEntity(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    photoUrl = it.photoUrl,
                    createdAt = it.createdAt,
                    lat = it.lat,
                    lon = it.lon
                )
            }
            val endOfPaginationReached = data.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH){
                    database.remoteKeyDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page-1
                val nextKey = if (endOfPaginationReached) null else page+1
                val keys = data.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeyDao().insertAll(keys)
                database.storyDao().insertStory(data)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (exception: Exception){
            return MediatorResult.Error(exception)
        }
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys?{
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeyDao().getRemoteKeysId(data.id)
        }
    }
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys?{
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeyDao().getRemoteKeysId(data.id)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys?{
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeyDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}