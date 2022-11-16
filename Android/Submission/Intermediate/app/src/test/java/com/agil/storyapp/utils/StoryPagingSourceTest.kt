package com.agil.storyapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.data.repository.Result

@OptIn(ExperimentalPagingApi::class)
class StoryPagingSourceTest : RemoteMediator<Int, LiveData<PagingData<StoryEntity>>>(){
    companion object{
        fun snapshot(item: List<StoryEntity>): PagingData<StoryEntity> {
            return PagingData.from(item)
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LiveData<PagingData<StoryEntity>>>
    ): MediatorResult {
        return try {
            MediatorResult.Success(endOfPaginationReached = true)
        }catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}


val noopListUpdateCategory = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}

    override fun onRemoved(position: Int, count: Int) {}

    override fun onMoved(fromPosition: Int, toPosition: Int) {}

    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}