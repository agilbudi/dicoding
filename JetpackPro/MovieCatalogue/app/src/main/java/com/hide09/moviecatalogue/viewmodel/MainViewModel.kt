package com.hide09.moviecatalogue.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hide09.moviecatalogue.model.Movie

class MainViewModel: ViewModel() {
    private val moviesList = MutableLiveData<ArrayList<Movie>>()

    fun setMovies(data: ArrayList<Movie>){
        moviesList.postValue(data)
    }

    fun getMoviesList(): LiveData<ArrayList<Movie>>{
        return moviesList
    }
}