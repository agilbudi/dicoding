package com.abupras.eventapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.abupras.eventapp.HomeNavActivity
import com.abupras.eventapp.databinding.FragmentHomeBinding
import com.abupras.eventapp.ui.DetailActivity

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

        homeViewModel.getAllEvents()
        homeViewModel.title.observe(viewLifecycleOwner){
            (activity as? HomeNavActivity)?.setTitle(it)
        }
        homeViewModel.isLoading.observe(viewLifecycleOwner){
            (activity as? HomeNavActivity)?.showLoading(it)
        }
        homeViewModel.listEvents.observe(viewLifecycleOwner){ events ->
            if (events != null) {
                if (events.error){
                    with(binding) {
                        showError(true)
                        tvHomeErrorMessage.text = events.message
                    }
                }else{
                    showError(false)
                    adapter.submitList(events.listEvents)
                }
            }
        }
        binding.btnHomeRetry.setOnClickListener {
            homeViewModel.getAllEvents()
        }
        binding.rvHome.adapter = adapter
        adapter.setOnItemClickCallback { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(EVENTS, event.id)
            startActivity(intent)
        }
        binding.svHome.setOnClickListener {
            binding.svHome.isIconified = false
        }
        return root
    }

    private fun showError(isError: Boolean){
        with(binding){
            if (isError){
                tvHomeErrorMessage.visibility = View.VISIBLE
                btnHomeRetry.visibility = View.VISIBLE
            }else{
                tvHomeErrorMessage.visibility = View.GONE
                btnHomeRetry.visibility = View.GONE
            }
        }
    }

    companion object{
        private const val TAG = "HomeFragment"
        private const val EVENTS = "EVENTS"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}