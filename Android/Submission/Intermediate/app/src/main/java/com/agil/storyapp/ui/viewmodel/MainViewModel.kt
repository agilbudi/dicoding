package com.agil.storyapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agil.storyapp.data.local.di.Injection
import com.agil.storyapp.data.repository.StoryRepository

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun story(token: String) = storyRepository.getPageStory(token)

}

class MainViewModelFactory private constructor(
    private val storyRepository: StoryRepository
): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(storyRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: "+ modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: MainViewModelFactory? = null

        fun getInstance(context: Context): MainViewModelFactory =
            instance?: synchronized(this){
                instance?: MainViewModelFactory(Injection.provideStoryRepository(context))
            }.also { instance = it }
    }
}