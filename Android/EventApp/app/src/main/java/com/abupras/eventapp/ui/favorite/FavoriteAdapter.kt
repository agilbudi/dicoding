package com.abupras.eventapp.ui.favorite

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.abupras.eventapp.R
import com.abupras.eventapp.data.local.entitiy.EventEntity
import com.abupras.eventapp.databinding.ItemFavoriteBinding
import com.abupras.eventapp.helper.EventDiffCallback
import com.abupras.eventapp.utils.changeFormatDate
import com.abupras.eventapp.utils.getPalette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar

class FavoriteAdapter(private val onFavoriteClick: (EventEntity) -> Unit): ListAdapter<EventEntity, FavoriteAdapter.FavoriteViewHolder>(EventDiffCallback) {

    private var onItemClickCallback: ((EventEntity) -> Unit)? = null

    fun setOnItemClickCallback(onItemClickCallback: (EventEntity) -> Unit){
        this.onItemClickCallback = onItemClickCallback
    }

    class FavoriteViewHolder(val binding: ItemFavoriteBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(event: EventEntity, onItemClick: (EventEntity) -> Unit){
            val glide = Glide.with(itemView.context)
            with(binding){
                tvFavoriteName.text = event.name
                tvFavoriteCategory.text = event.category
                tvFavoriteCityTime.text = "${event.cityName} • ${changeFormatDate(event.beginTime)}"
                tvFavoriteQuota.text = "Quota: ${event.quota - event.registrants} left"
                tvFavoriteSummary.text = event.summary
                ibFavoriteExpand.isActivated = false
                tvFavoriteSummary.visibility = View.GONE
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
                            ivFavoriteCover.setImageDrawable(itemView.context.getDrawable(R.drawable.icon_no_image_24))
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
                            val palette = getPalette(resource)
                            val color = palette.vibrantSwatch?.rgb ?: COLOR_DEFAULT
                            val textColor = palette.vibrantSwatch?.titleTextColor ?: Color.BLACK
                            layoutItemFavorite.setBackgroundColor(color)
                            tvFavoriteSummary.setTextColor(textColor)
                            return false
                        }
                    })
                    .into(ivFavoriteCover)

                ibFavoriteExpand.setOnClickListener {
                    toggleExpand(binding)
                }
                root.setOnClickListener {
                    if (ibFavoriteExpand.isActivated){
                        toggleExpand(binding)
                    }else {
                        onItemClick(event)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val event = getItem(position)
        val ibFavorite = holder.binding.ibFavorite
        if (event.isFavorite){
            toggleFavorite(holder.binding)
        }else{
            toggleFavorite(holder.binding)
        }
        ibFavorite.setOnClickListener {
            toggleFavorite(holder.binding)
            onFavoriteClick(event)
            val message = if (ibFavorite.isActivated) "Ditambahkan ke favorit" else "Dihapus dari favorit"
            Snackbar.make(ibFavorite, message, Snackbar.LENGTH_SHORT).show()
        }
        holder.bind(event){ clickEvent ->
            onItemClickCallback?.invoke(clickEvent)
        }

    }


    companion object{
        private const val COLOR_DEFAULT = Color.LTGRAY
        private const val TAG = "FavoriteAdapter"

        private fun toggleFavorite(binding: ItemFavoriteBinding) {
            with(binding){
                val isFavorite = !ibFavorite.isActivated
                ibFavorite.isActivated = isFavorite
            }
        }
        private fun toggleExpand(binding: ItemFavoriteBinding) {
            with(binding){
                val isExpanded = !ibFavoriteExpand.isActivated
                ibFavoriteExpand.isActivated = isExpanded
                tvFavoriteSummary.visibility = if (isExpanded) View.VISIBLE else View.GONE
            }
            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, AutoTransition())
        }
    }
}