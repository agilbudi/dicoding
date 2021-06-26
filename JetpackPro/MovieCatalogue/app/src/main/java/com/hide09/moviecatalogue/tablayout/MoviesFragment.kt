package com.hide09.moviecatalogue.tablayout

import android.os.Bundle
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
    private var listMovie: ArrayList<Movie>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesTabVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        val movies: Array<String> = resources.getStringArray(R.array.movies)
        for (i in movies){
            listMovie?.add(Movie("Title","R.drawable.poster_creed","2021","ini deskripsinya"))
        }
        val gridlayout = GridLayoutManager(activity,2)

        binding.rvMovies.apply {
            setHasFixedSize(true)
            layoutManager = gridlayout
            adapter = tabAdapter
        }
        showData(listMovie!!)
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