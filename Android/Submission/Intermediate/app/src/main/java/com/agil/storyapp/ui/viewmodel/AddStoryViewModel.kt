package com.agil.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agil.storyapp.data.local.di.Injection
import com.agil.storyapp.data.repository.AddStoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val addStoryRepository: AddStoryRepository) : ViewModel() {

    fun uploadStory(
        token: String, imageMultipart: MultipartBody.Part, description: RequestBody,
        lat: Float?, lon: Float?
    ) = addStoryRepository.uploadData(token, imageMultipart, description, lat, lon)

}

@Suppress("UNCHECKED_CAST")
class AddStoryViewModelFactory private constructor(private val addStoryRepository: AddStoryRepository):
    ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)){
            return AddStoryViewModel(addStoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: "+ modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: AddStoryViewModelFactory? = null

        fun getInstance(): AddStoryViewModelFactory =
            instance ?: synchronized(this){
                instance ?: AddStoryViewModelFactory(Injection.provideAddStoryRepository())
            }.also { instance = it }
    }
}