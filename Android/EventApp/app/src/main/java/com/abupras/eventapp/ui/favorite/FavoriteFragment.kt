package com.abupras.eventapp.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.abupras.eventapp.R
import com.abupras.eventapp.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }
}