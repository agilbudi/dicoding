package com.agil.storyapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.agil.storyapp.data.local.di.Injection
import com.agil.storyapp.data.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun login(email: String, password: String) = authRepository.login(email, password)

    fun register(name: String, email: String, password: String) =
        authRepository.register(name, email, password)

    fun setEmail(email: String)= authRepository.setEmail(email)
    fun getEmail() = authRepository.getEmail()
}

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory private constructor(private val authRepository: AuthRepository):
    ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(authRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: "+ modelClass.name)
    }

    companion object{
        @Volatile
        private var instance: AuthViewModelFactory? = null

        fun getInstance(): AuthViewModelFactory =
            instance ?: synchronized(this){
                instance ?: AuthViewModelFactory(Injection.provideAuthRepository())
            }.also { instance = it }
    }
}