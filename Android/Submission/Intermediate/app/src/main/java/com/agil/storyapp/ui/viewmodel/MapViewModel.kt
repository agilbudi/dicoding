package com.agil.storyapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agil.storyapp.data.local.di.Injection
import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.data.repository.MapStoryRepository
import com.agil.storyapp.data.repository.Result

class MapViewModel(private val mapStoryRepository: MapStoryRepository): ViewModel() {

    fun listStory(token: String) = mapStoryRepository.getListOnMapStory(token)
    fun markerStory(): LiveData<Result<List<StoryEntity>>> = mapStoryRepository.getMapStory()

}

class MapViewModelFactory private constructor(
    private val mapStoryRepository: MapStoryRepository
): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)){
            return MapViewModel(mapStoryRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: "+ modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: MapViewModelFactory? = null

        fun getInstance(context: Context): MapViewModelFactory =
            instance ?: synchronized(this){
                instance ?: MapViewModelFactory(Injection.provideMapRepository(context))
            }.also { instance = it }
    }
}