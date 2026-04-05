package com.abupras.eventapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.abupras.eventapp.data.local.entitiy.EventEntity

object EventDiffCallback : DiffUtil.ItemCallback<EventEntity>() {
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