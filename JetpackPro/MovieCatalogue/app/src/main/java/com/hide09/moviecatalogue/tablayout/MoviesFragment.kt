package com.hide09.moviecatalogue.tablayout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hide09.moviecatalogue.R
import com.hide09.moviecatalogue.adapter.TabAdapter
import com.hide09.moviecatalogue.databinding.FragmentMoviesBinding
import com.hide09.moviecatalogue.model.Movie
import com.hide09.moviecatalogue.viewmodel.MainViewModel

class MoviesFragment : Fragment() {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var moviesTabVM: MainViewModel
    private val tabAdapter = TabAdapter()
    private var listMovie: ArrayList<Movie> = arrayListOf()

    companion object{
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesTabVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        val gridlayout = GridLayoutManager(activity,2)
        loadData()
        binding.rvMovies.apply {
            setHasFixedSize(true)
            layoutManager = gridlayout
            adapter = tabAdapter
        }
        Log.e("DATA", listMovie.toString())
        showData(listMovie)
    }

    private fun loadData() {
        listMovie.add(Movie("title",R.drawable.poster_creed,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_a_start_is_born,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_alita,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_aquaman,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_bohemian,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_cold_persuit,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_crimes,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_glass,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_how_to_train,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_infinity_war,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_marry_queen,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_master_z,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_mortal_engines,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_overlord,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_ralph,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_robin_hood,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_serenity,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_spiderman,2021, "des"))
        listMovie.add(Movie("title",R.drawable.poster_t34,2021, "des"))
    }

    private fun showData(listMovie: ArrayList<Movie>) {
        moviesTabVM.setMovies(listMovie)
        moviesTabVM.getMoviesList().observe(requireActivity(), {item ->
            if (item != null){
                tabAdapter.update(item)
            }
        })
    }
}