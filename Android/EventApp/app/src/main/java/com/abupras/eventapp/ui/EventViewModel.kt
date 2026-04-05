package com.abupras.eventapp.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abupras.eventapp.data.EventRepository
import com.abupras.eventapp.data.local.entitiy.EventEntity
import com.abupras.eventapp.di.Injection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String> = _snackbarMessage

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch {
            delay(1000)
            eventRepository.setFavoriteEvent(event, false)
        }
        _snackbarMessage.value = "Perubahan favorite ditetapkan"
    }
    fun saveEvent(event: EventEntity) {
        viewModelScope.launch{
            delay(1000)
            eventRepository.setFavoriteEvent(event, true)
        }
        _snackbarMessage.value = "Perubahan favorite ditetapkan"
    }
    fun getAllEvent() = eventRepository.getAllEvent()
    fun getSearchEventNew(active: Int, query: String) = eventRepository.getSearchEvent(active, query)
    fun getEvent(active: Int) = eventRepository.getEventActive(active)
    fun getFavoriteEvent() = eventRepository.getFavoriteEvent()
    fun getDetailEvent(eventId: Int) = eventRepository.getDetailEvent(eventId)
    fun setFavorite(id: Int, stateFavorite: Boolean){
        viewModelScope.launch{
            eventRepository.updateFavorite(id, stateFavorite)
        }
        _isFavorite.value = stateFavorite
        _snackbarMessage.value = if (stateFavorite) "Favorit Ditambahkan" else "Favorit Dihapus"
    }
}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(private val eventRepository: EventRepository) :
        ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(EventViewModel::class.java)){
                    return EventViewModel(eventRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
        }