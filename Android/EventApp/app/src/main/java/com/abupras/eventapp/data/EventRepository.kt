package com.abupras.eventapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.abupras.eventapp.data.local.entitiy.EventEntity
import com.abupras.eventapp.data.local.room.EventDao
import com.abupras.eventapp.data.remote.response.EventResponse
import com.abupras.eventapp.data.remote.retrofit.ApiService

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
) {

    fun getAllEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllEvents()
            val eventList = getDataEvent(response)
            eventDao.deleteAll()
            eventDao.insert(eventList)
        } catch (e: Exception) {
            Log.d(TAG, "getAllEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventEntity>>> = eventDao.getEvent().map {
            Result.Success(it)
        }
        emitSource(localData)
    }

    fun getDetailEvent(eventId: Int): LiveData<Result<EventEntity>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetail(eventId)
            val event = response.event
            val isFavorite = eventDao.isFavorite(eventId)
            val detailEvent = EventEntity(
                eventId = event.id,
                summary = event.summary,
                description = event.description,
                mediaCover = event.mediaCover,
                ownerName = event.ownerName,
                imageLogo = event.imageLogo,
                cityName = event.cityName,
                quota = event.quota,
                registrants = event.registrants,
                name = event.name,
                beginTime = event.beginTime,
                endTime = event.endTime,
                category = event.category,
                link = event.link,
                isFavorite = isFavorite
            )
            emit(Result.Success(detailEvent))
        } catch (e: Exception) {
            Log.d(TAG, "getAllEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getEventActive(active: Int): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getActiveEvents(active)
            val eventList = getDataEvent(response)
            emit(Result.Success(eventList))
        } catch (e: Exception) {
            Log.d(TAG, "getAllEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getSearchEvent(active: Int, query: String): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getSearch(active, query)
            val eventList = getDataEvent(response)
            emit(Result.Success(eventList))
        } catch (e: Exception) {
            Log.d(TAG, "getAllEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFavoriteEvent(): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val localData: LiveData<Result<List<EventEntity>>> = eventDao.getFavoriteEvent().map {
                Result.Success(it)
            }
            emitSource(localData)
        } catch (e: Exception) {
            Log.d(TAG, "getAllEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun setFavoriteEvent(event: EventEntity, favoriteState: Boolean) {
        event.isFavorite = favoriteState
        eventDao.update(event)
    }

    suspend fun updateFavorite(id: Int, favoriteState: Boolean): Boolean {
        try {
            eventDao.updateFavoriteStatus(id, favoriteState)
        } catch (e: Exception) {
            Log.d(TAG, "updateFavorite: ${e.message.toString()}")
            return false
        }
        return true
    }

    private suspend fun getDataEvent(response: EventResponse): List<EventEntity> {
        val events = response.listEvents
        val eventList = events.map { event ->
            val isFavorite = eventDao.isFavorite(event.id)
            EventEntity(
                eventId = event.id,
                summary = event.summary,
                description = event.description,
                mediaCover = event.mediaCover,
                ownerName = event.ownerName,
                imageLogo = event.imageLogo,
                cityName = event.cityName,
                quota = event.quota,
                registrants = event.registrants,
                name = event.name,
                beginTime = event.beginTime,
                endTime = event.endTime,
                category = event.category,
                link = event.link,
                isFavorite = isFavorite
            )
        }
        return eventList
    }

    companion object {
        private const val TAG = "EventRepository"

        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao
        ): EventRepository = instance ?: synchronized(this) {
            instance ?: EventRepository(apiService, eventDao)
        }.also { instance = it }
    }
}