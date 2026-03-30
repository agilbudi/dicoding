package com.abupras.eventapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class EventAdapter<T : Any, VB : ViewBinding>(
    private val bindingFactory: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val bind: (VB, T) -> Unit,
    private val onItemClick: ((T) -> Unit)? = null,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, EventAdapter.EventViewHolder<VB>>(diffCallback){

    class EventViewHolder<VB : ViewBinding>(val binding: VB): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder<VB> {
        val binding = bindingFactory(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder<VB>, position: Int) {
        val item = getItem(position)
        bind(holder.binding, item)
        holder.itemView.setOnClickListener { onItemClick?.invoke(item) }
    }

}