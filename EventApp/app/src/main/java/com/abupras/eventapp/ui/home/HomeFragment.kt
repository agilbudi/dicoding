package com.abupras.eventapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.abupras.eventapp.HomeNavActivity
import com.abupras.eventapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvHome.layoutManager = layoutManager
        val adapter = HomeAdapter()

        homeViewModel.title.observe(viewLifecycleOwner){
            (activity as? HomeNavActivity)?.setTitle(it)
        }
        homeViewModel.isLoading.observe(viewLifecycleOwner){
            (activity as? HomeNavActivity)?.showLoading(it)
        }
        homeViewModel.listEvents.observe(viewLifecycleOwner){ events ->
            if (events != null) {
                adapter.submitList(events)
            }
        }

        binding.rvHome.adapter = adapter
        binding.svHome.setOnClickListener {
            binding.svHome.isIconified = false
        }
        return root
    }



    companion object{
    private const val TAG = "HomeFragment"
}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}