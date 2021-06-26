package com.hide09.moviecatalogue.tablayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hide09.moviecatalogue.R
import com.hide09.moviecatalogue.databinding.FragmentTvShowBinding
import com.hide09.moviecatalogue.model.Movie

class TvShowFragment : Fragment() {
    private var _binding: FragmentTvShowBinding? = null
    private val binding get() = _binding!!
    private lateinit var listTvShow: ArrayList<Movie>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvShow: Array<String> = resources.getStringArray(R.array.tv_shows)

        for (i in tvShow){
            listTvShow.add(Movie("Title",i,"2021","ini deskripsinya"))
        }
    }

}