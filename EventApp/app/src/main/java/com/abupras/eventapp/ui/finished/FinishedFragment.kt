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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.abupras.eventapp.HomeNavActivity
import com.abupras.eventapp.R
import com.abupras.eventapp.data.response.ListEventsItem
import com.abupras.eventapp.databinding.FragmentFinishedBinding
import com.abupras.eventapp.databinding.ItemEventBinding
import com.abupras.eventapp.ui.DetailActivity
import com.abupras.eventapp.ui.EventAdapter
import com.abupras.eventapp.utils.changeFormatDate
import com.abupras.eventapp.utils.getPalette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null

    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val finishedViewModel =
            ViewModelProvider(this)[FinishedViewModel::class.java]

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinished.layoutManager = layoutManager

        val adapter = EventAdapter(
            bindingFactory = ItemEventBinding::inflate,
            bind = { binding, item ->
                setDataView(binding, item)
            },
            onItemClick = { event ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EVENTS, event.id)
                startActivity(intent)
            },
            diffCallback = EventDiffCallback
        )
        //finishedViewModel.getFinishedEvent(EVENT_FINISHED)
        finishedViewModel.title.observe(viewLifecycleOwner){
            (activity as? HomeNavActivity)?.setTitle(it)
        }
        finishedViewModel.isLoading.observe(viewLifecycleOwner){
            (activity as? HomeNavActivity)?.showLoading(it)
        }
        finishedViewModel.listEvents.observe(viewLifecycleOwner){ events ->
            if (events != null){
                if (events.error){
                    with(binding){
                        adapter.notifyDataSetChanged()
                        showError(true)
                        tvFinishedErrorMessage.text = events.message
                    }
                }else{
                    showError(false)
                    adapter.submitList(events.listEvents)
                }
            }
        }
        binding.btnFinishedRetry.setOnClickListener {
            finishedViewModel.getFinishedEvent(EVENT_FINISHED)
        }

        binding.rvFinished.adapter = adapter

        return root
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
    private fun setDataView(binding: ItemEventBinding, item: ListEventsItem) {
        val glide = Glide.with(requireContext())
        with(binding){
            tvName.text = item.name
            tvCategory.text = item.category
            tvCityTime.text = "${item.cityName} • ${changeFormatDate(item.beginTime)}"
            tvQuota.text = "Quota: ${item.quota} left"
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
                        layoutItem.setBackgroundColor(color)
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
        object EventDiffCallback : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}