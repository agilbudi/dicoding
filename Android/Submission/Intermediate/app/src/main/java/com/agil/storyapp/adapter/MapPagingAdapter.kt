package com.agil.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.agil.storyapp.R
import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.databinding.ItemMapStoriesBinding
import com.agil.storyapp.utils.withDateFormat
import com.bumptech.glide.Glide

class MapPagingAdapter: PagingDataAdapter<StoryEntity, MapPagingAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var onItemClickCallBack: OnItemClickCallback? = null

    inner class MyViewHolder(private val binding: ItemMapStoriesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryEntity) {
            var imgUrl: Any? = ContextCompat.getDrawable(itemView.context, R.mipmap.ic_image_default)
            if (data.photoUrl.isNotEmpty() || data.photoUrl != "") {
                imgUrl = data.photoUrl
            }
            with(binding){
                Glide.with(itemView.context)
                    .load(imgUrl)
                    .centerCrop()
                    .into(ivItemPhoto)
                tvItemName.text = data.name
                tvItemDate.text = data.createdAt.withDateFormat()
                ibItemDetail.setOnClickListener { onItemClickCallBack?.onDetailItemClicked(data) }
                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(data) }
            }
        }

    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallBack = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMapStoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: StoryEntity)
        fun onDetailItemClicked(data: StoryEntity)
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>(){
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}