package com.abupras.eventapp.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.abupras.eventapp.HomeNavActivity
import com.abupras.eventapp.data.Result
import com.abupras.eventapp.data.local.entitiy.EventEntity
import com.abupras.eventapp.databinding.FragmentFavoriteBinding
import com.abupras.eventapp.ui.DetailActivity
import com.abupras.eventapp.ui.EventViewModel
import com.abupras.eventapp.ui.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: EventViewModel by viewModels {
            factory
        }
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = FavoriteAdapter { event ->
            if (event.isFavorite) {
                viewModel.deleteEvent(event)
            } else {
                viewModel.saveEvent(event)
            }
        }

        binding.rvFavorite.layoutManager = layoutManager
        binding.btnFavoriteRetry.setOnClickListener {
            viewModel.getFavoriteEvent()
        }
        binding.rvFavorite.adapter = adapter

        (activity as? HomeNavActivity)?.setTitle("Favorite Events")
        viewModel.getFavoriteEvent().observe(viewLifecycleOwner) { event ->
            adapter.showAllFavorite(event)
        }
        adapter.setOnItemClickCallback { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(EVENTS, event.eventId)
            startActivity(intent)
        }
    }

    private fun FavoriteAdapter.showAllFavorite(result: Result<List<EventEntity>>) {
        when (result) {
            is Result.Loading -> {
                showError(true)
            }

            is Result.Error -> {
                showError(false)
            }

            is Result.Success -> {
                lifecycleScope.launch {
                    delay(500)
                    showError(false)
                    this@showAllFavorite.submitList(result.data)
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showError(show: Boolean) {
        (activity as? HomeNavActivity)?.showLoading(show)
    }

    companion object {
        private const val EVENTS = "EVENTS"

    }
}

