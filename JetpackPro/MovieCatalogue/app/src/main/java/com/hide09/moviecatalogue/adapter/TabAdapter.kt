package com.hide09.moviecatalogue.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hide09.moviecatalogue.databinding.LayoutItemBinding
import com.hide09.moviecatalogue.model.Movie

class TabAdapter: RecyclerView.Adapter<TabAdapter.ListViewHolder>() {
    private val listMovie = ArrayList<Movie>()


    fun update(data: ArrayList<Movie>) {
        listMovie.clear()
        listMovie.addAll(data)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(private val binding: LayoutItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding){
                Glide.with(itemView.context)
                    .load(movie.image)
                    .into(ivItemImage)
                tvItemYear.text = movie.year
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }

    override fun getItemCount(): Int = listMovie.size
}