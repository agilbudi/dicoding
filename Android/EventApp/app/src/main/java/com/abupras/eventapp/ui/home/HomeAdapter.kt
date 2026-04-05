package com.abupras.eventapp.ui.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abupras.eventapp.R
import com.abupras.eventapp.data.local.entitiy.EventEntity
import com.abupras.eventapp.databinding.ItemHomeBinding
import com.abupras.eventapp.ui.home.HomeAdapter.HomeViewHolder
import com.abupras.eventapp.utils.changeFormatDate
import com.abupras.eventapp.utils.getPalette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class HomeAdapter: ListAdapter<EventEntity, HomeViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallback: ((EventEntity) -> Unit)? = null

    fun setOnItemClickCallback(onItemClickCallback: (EventEntity) -> Unit){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onItemClickCallback?.invoke(event)
        }
    }

    class HomeViewHolder(private val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(event: EventEntity){
            val glide = Glide.with(itemView.context)
            with(binding){
                tvHomeName.text = event.name
                tvHomeCategory.text = event.category
                tvHomeCityTime.text = "${event.cityName} • ${changeFormatDate(event.beginTime)}"
                glide.asBitmap()
                    .load(event.imageLogo)
                    .listener(object : RequestListener<Bitmap>{
                        @SuppressLint("UseCompatLoadingForDrawables")
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>,
                            isFirstResource: Boolean
                        ): Boolean {
                            ivHomeCover.setImageDrawable(itemView.context.getDrawable(R.drawable.icon_no_image_24))
                            Log.e(TAG, e?.message.toString())
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            model: Any,
                            target: Target<Bitmap>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            ivHomeCover.setImageBitmap(resource)
                            val palette = getPalette(resource)
                            val color = palette.vibrantSwatch?.rgb ?: COLOR_DEFAULT
                            layoutItemHome.setBackgroundColor(color)
                            val textColor = palette.vibrantSwatch?.titleTextColor ?: Color.BLACK
                            tvHomeName.setTextColor(textColor)
                            tvHomeCityTime.setTextColor(textColor)
                            return false
                        }

                    })
                    .into(ivHomeCover)
            }
        }
    }

    companion object {
        private const val COLOR_DEFAULT = Color.LTGRAY
        private const val TAG = "EventAdapter"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>(){
            override fun areItemsTheSame(
                oldItem: EventEntity,
                newItem: EventEntity
            ): Boolean {
                return oldItem.eventId == newItem.eventId
            }

            override fun areContentsTheSame(
                oldItem: EventEntity,
                newItem: EventEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}
