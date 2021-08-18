package com.hide09.androidfundamental.navigationevent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.hide09.androidfundamental.R
import com.hide09.androidfundamental.databinding.FragmentHomeNavBinding


class HomeNavFragment : Fragment() {
    private var _binding: FragmentHomeNavBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeNavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCategoryNav.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeNavFragment_to_categoryNavFragment)
        )
        binding.btnProfileNav.setOnClickListener{ view ->
            view.findNavController().navigate(R.id.action_homeNavFragment_to_profileActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}