package com.abupras.eventapp.ui.finished

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.abupras.eventapp.HomeNavActivity
import com.abupras.eventapp.R
import com.abupras.eventapp.data.Result
import com.abupras.eventapp.data.local.entitiy.EventEntity
import com.abupras.eventapp.databinding.FragmentFinishedBinding
import com.abupras.eventapp.databinding.ItemEventBinding
import com.abupras.eventapp.helper.EventDiffCallback
import com.abupras.eventapp.ui.DetailActivity
import com.abupras.eventapp.ui.EventAdapter
import com.abupras.eventapp.ui.EventViewModel
import com.abupras.eventapp.ui.ViewModelFactory
import com.abupras.eventapp.utils.changeFormatDate
import com.abupras.eventapp.utils.getPalette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModel: EventViewModel by viewModels {
            factory
        }
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = EventAdapter(
            bindingFactory = ItemEventBinding::inflate,
            bind = { binding, item ->
                setDataView(binding, item)
            },
            onItemClick = { event ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EVENTS, event.eventId)
                startActivity(intent)
            },
            diffCallback = EventDiffCallback
        )
        binding.rvFinished.layoutManager = layoutManager
        binding.rvFinished.adapter = adapter
        (activity as? HomeNavActivity)?.setTitle("Finished Events")

        viewModel.getEvent(EVENT_FINISHED).observe(viewLifecycleOwner){ event ->
            adapter.showAllEvent(event)
        }
        binding.btnFinishedRetry.setOnClickListener {
            viewModel.getEvent(EVENT_FINISHED)
        }
    }

    private fun EventAdapter<EventEntity, ItemEventBinding>.showAllEvent(event: Result<List<EventEntity>>?) {
        if (event != null){
            when(event){
                is Result.Loading -> {
                    (activity as? HomeNavActivity)?.showLoading(true)
                }
                is Result.Error -> {
                    (activity as? HomeNavActivity)?.showLoading(false)
                    showError(true)
                }
                is Result.Success -> {
                    lifecycleScope.launch {
                        delay(500)
                        (activity as? HomeNavActivity)?.showLoading(false)
                        showError(false)
                        this@showAllEvent.submitList(event.data)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun showError(isError: Boolean){
        with(binding) {
            if (isError) {
                tvFinishedErrorMessage.visibility = View.VISIBLE
                btnFinishedRetry.visibility = View.VISIBLE
            }else{
                tvFinishedErrorMessage.visibility = View.GONE
                btnFinishedRetry.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataView(binding: ItemEventBinding, item: EventEntity) {
        val glide = Glide.with(requireContext())
        with(binding){
            tvName.text = item.name
            tvCategory.text = item.category
            tvCityTime.text = "${item.cityName} • ${changeFormatDate(item.beginTime)}"
            tvQuota.text = "Participant: ${item.registrants}"
            tvSummary.text = item.summary
            glide.load(item.imageLogo).into(ivImageLogo)
            glide.asBitmap()
                .load(item.mediaCover)
                .listener(object : RequestListener<Bitmap> {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>,
                        isFirstResource: Boolean,
                    ): Boolean {
                        ivMediaCover.setImageDrawable(requireContext().getDrawable(R.drawable.icon_no_image_24))
                        Log.e(TAG, e?.message.toString())
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        model: Any,
                        target: Target<Bitmap>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean,
                    ): Boolean {
                        val palette = getPalette(resource).vibrantSwatch
                        val color = palette?.rgb ?: COLOR_DEFAULT
                        layoutItemEvent.setBackgroundColor(color)
                        val textColor = palette?.titleTextColor ?: Color.BLACK
                        tvName.setTextColor(textColor)
                        tvCityTime.setTextColor(textColor)
                        tvQuota.setTextColor(textColor)
                        tvSummary.setTextColor(textColor)
                        return false
                    }
                })
                .into(ivMediaCover)
        }
    }

    companion object{
        private const val COLOR_DEFAULT = Color.LTGRAY
        private const val TAG = "FinishedFragment"
        private const val EVENT_FINISHED = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}