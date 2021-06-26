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
import com.hide09.moviecatalogue.databinding.FragmentTvShowBinding
import com.hide09.moviecatalogue.model.Movie
import com.hide09.moviecatalogue.viewmodel.MainViewModel

class TvShowFragment : Fragment() {
    private var _binding: FragmentTvShowBinding? = null
    private val binding get() = _binding!!
    private lateinit var tvShowTabVM: MainViewModel
    private val tabAdapter = TabAdapter()
    private var listTvShow: ArrayList<Movie> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvShowTabVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
        val gridlayout = GridLayoutManager(activity,2)
        loadData()
        binding.rvTvshow.apply {
            setHasFixedSize(true)
            layoutManager = gridlayout
            adapter = tabAdapter
        }
        showData(listTvShow)

    }

    private fun showData(listTvShow: ArrayList<Movie>) {
        tvShowTabVM.setMovies(listTvShow)
        tvShowTabVM.getMoviesList().observe(requireActivity(), { items ->
            if (items != null){
                tabAdapter.update(items)
            }
        })
    }

    private fun loadData() {
            listTvShow.add(Movie("title", R.drawable.tv_poster_arrow, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_doom_patrol, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_dragon_ball, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_fairytail, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_family_guy, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_flash, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_god, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_gotham, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_grey_anatomy, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_hanna, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_iron_fist, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_naruto_shipudden, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_ncis, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_riverdale, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_shameless, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_supergirl, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_supernatural, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_the_simpson, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_the_umbrella, 2021, "desc"))
            listTvShow.add(Movie("title", R.drawable.tv_poster_the_walking_dead, 2021, "desc"))
    }

}