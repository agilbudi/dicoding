package com.hide09.githubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hide09.githubapp.databinding.ItemUserBinding
import com.hide09.githubapp.model.User

class UserListAdapter: RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {
    private val listUser =  ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun updateUsers(newUsers : ArrayList<User>){
        listUser.clear()
        listUser.addAll(newUsers)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
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
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    interface  OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}


