package com.hide09.consumergithub.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hide09.consumergithub.databinding.ItemUserBinding
import com.hide09.consumergithub.entity.Favorite

class FavoriteListAdapter: RecyclerView.Adapter<FavoriteListAdapter.ListViewHolder>() {
    private val listFavorite =  ArrayList<Favorite>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun updateFavorite(newUsers : ArrayList<Favorite>){
        listFavorite.clear()
        listFavorite.addAll(newUsers)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: Favorite) {
            with(binding){
                Glide.with(itemView.context)
                    .load(items.photo)
                    .apply(RequestOptions().override(90,90))
                    .apply(RequestOptions().centerCrop())
                    .into(civUser)
                tvItemName.text = items.username
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(items) }
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int = listFavorite.size

    interface  OnItemClickCallback {
        fun onItemClicked(item: Favorite)
    }
}


