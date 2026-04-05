package com.abupras.eventapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeNavViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
    fun getTitleSettings(): LiveData<String> {
        return pref.getTitleSetting().asLiveData()
    }
    fun saveTitleSetting(title: String){
        viewModelScope.launch{
            pref.saveTitleSetting(title)
        }
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch{
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val pref: SettingPreferences) : NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeNavViewModel::class.java)){
            return HomeNavViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: "+ modelClass.name)
    }
}