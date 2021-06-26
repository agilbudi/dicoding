package com.hide09.consumergithub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hide09.consumergithub.entity.Favorite

class UserFavoriteViewModel: ViewModel() {
    private val favoriteList = MutableLiveData<ArrayList<Favorite>>()

    fun setFavorite(data: ArrayList<Favorite>){
        favoriteList.postValue(data)
    }

    fun getFavorite(): LiveData<ArrayList<Favorite>>{
        return favoriteList
    }
}