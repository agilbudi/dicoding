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
import com.abupras.eventapp.data.response.ListEventsItem
import com.abupras.eventapp.databinding.ItemHomeBinding
import com.abupras.eventapp.ui.home.HomeAdapter.MyViewHolder
import com.abupras.eventapp.utils.changeFormatDate
import com.abupras.eventapp.utils.getPalette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class HomeAdapter: ListAdapter<ListEventsItem, MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(private val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(event: ListEventsItem){
            val glide = Glide.with(itemView.context)

            with(binding){
                tvName.text = event.name
                tvCategory.text = event.category
                tvCityTime.text = "${event.cityName} • ${changeFormatDate(event.beginTime)}"
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
                            ivMediaCover.setImageDrawable(itemView.context.getDrawable(R.drawable.icon_no_image_24))
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
                            ivMediaCover.setImageBitmap(resource)
                            val palette = getPalette(resource)
                            val color = palette.vibrantSwatch?.rgb ?: COLOR_DEFAULT
                            layoutItem.setBackgroundColor(color)
                            val textColor = palette.vibrantSwatch?.titleTextColor ?: Color.BLACK
                            tvName.setTextColor(textColor)
                            tvCityTime.setTextColor(textColor)
                            return false
                        }

                    })
                    .into(ivMediaCover)
            }
        }
    }

    companion object {
        private const val COLOR_DEFAULT = Color.LTGRAY
        private const val TAG = "EventAdapter"
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>(){
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}
