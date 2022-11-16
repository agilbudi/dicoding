package com.agil.storyapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.agil.storyapp.data.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<StoryEntity>)

    @Update
    fun update(story: StoryEntity)

    @Delete
    fun delete(story: StoryEntity)

    @Query("SELECT * FROM storyentity")
    fun allStory(): PagingSource<Int,StoryEntity>

    @Query("SELECT * FROM storyentity")
    fun mapStory(): LiveData<List<StoryEntity>>

    @Query("DELETE FROM storyentity")
    suspend fun deleteAll()
}