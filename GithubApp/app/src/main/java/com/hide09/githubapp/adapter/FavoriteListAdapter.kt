package com.hide09.githubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hide09.githubapp.databinding.ItemUserBinding
import com.hide09.githubapp.entity.Favorite

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
        fun bind(user: Favorite) {
            with(binding){
                Glide.with(itemView.context)
                    .load(user.photo)
                    .apply(RequestOptions().override(90,90))
                    .apply(RequestOptions().centerCrop())
                    .into(civUser)
                tvItemName.text = user.username
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
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
        fun onItemClicked(data: Favorite)
    }
}


