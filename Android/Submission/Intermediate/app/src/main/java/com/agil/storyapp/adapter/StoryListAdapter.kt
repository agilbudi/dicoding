package com.agil.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agil.storyapp.R
import com.agil.storyapp.databinding.ItemStoriesBinding
import com.agil.storyapp.model.Story
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class StoryListAdapter: RecyclerView.Adapter<StoryListAdapter.ListViewHolder>() {
    private val listStory = ArrayList<Story>()
    private var onItemClickCallBack: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallBack = onItemClickCallback
    }

    fun updateStory(newStory: ArrayList<Story>){
        listStory.clear()
        listStory.addAll(newStory)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ItemStoriesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            var imgUrl: Any? = R.mipmap.ic_image_default
            if (!story.photoUrl.isNullOrEmpty() || story.photoUrl != ""){
                imgUrl = story.photoUrl
            }
            with(binding){
                Glide.with(itemView.context)
                    .load(imgUrl)
                    .apply(RequestOptions().centerCrop())
                    .into(ivItemPhoto)
                tvItemName.text = story.name
                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(story) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    interface  OnItemClickCallback {
        fun onItemClicked(data: Story)
    }
}