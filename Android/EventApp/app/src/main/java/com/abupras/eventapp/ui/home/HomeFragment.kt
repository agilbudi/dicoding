package com.abupras.eventapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.abupras.eventapp.HomeNavActivity
import com.abupras.eventapp.data.Result
import com.abupras.eventapp.data.local.entitiy.EventEntity
import com.abupras.eventapp.databinding.FragmentHomeBinding
import com.abupras.eventapp.ui.DetailActivity
import com.abupras.eventapp.ui.DetailActivity.Companion.EVENTS
import com.abupras.eventapp.ui.EventViewModel
import com.abupras.eventapp.ui.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.Companion.getInstance(requireContext())
        val eventViewModel: EventViewModel by viewModels {
            factory
        }

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvHome.layoutManager = layoutManager
        val adapter = HomeAdapter()
        (activity as? HomeNavActivity)?.setTitle("All Event")


        eventViewModel.getAllEvent().observe(viewLifecycleOwner){
            adapter.showAllEvent(it)
        }

        binding.btnHomeRetry.setOnClickListener {
            eventViewModel.getAllEvent().observe(viewLifecycleOwner){
                adapter.showAllEvent(it)
            }
        }

        binding.rvHome.adapter = adapter
        adapter.setOnItemClickCallback { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(EVENTS, event.eventId)
            startActivity(intent)
        }
        binding.svHome.setOnClickListener {
            binding.svHome.isIconified = false
        }
        binding.svHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    eventViewModel.getSearchEventNew(-1, query).observe(viewLifecycleOwner){ event ->
                        adapter.showAllEvent(event)
                    }
                    with(binding) {
                        svHome.clearFocus()
                        fabHomeAllEvent.visibility = View.VISIBLE
                        fabHomeAllEvent.setOnClickListener {
                            eventViewModel.getAllEvent().observe(viewLifecycleOwner){ event ->
                                adapter.showAllEvent(event)
                            }
                            fabHomeAllEvent.visibility = View.GONE
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


    }

    fun HomeAdapter.showAllEvent(event: Result<List<EventEntity>>?) {
            if (event != null){
                when(event){
                    is Result.Loading -> {
                        (activity as? HomeNavActivity)?.showLoading(true)
                    }
                    is Result.Error -> {
                        (activity as? HomeNavActivity)?.showLoading(false)
                        Toast.makeText(
                            activity,
                            "Terjadi kesalahan" + event.error,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, "Terjadi kesalahan: ${event.error}")
                        showError(true)
                    }
                    is Result.Success -> {
                        (activity as? HomeNavActivity)?.showLoading(false)
                        showError(false)
                        this.submitList(event.data)
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

