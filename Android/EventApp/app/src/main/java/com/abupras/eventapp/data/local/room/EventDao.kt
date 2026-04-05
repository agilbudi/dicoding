package com.abupras.eventapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.abupras.eventapp.data.local.entitiy.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: List<EventEntity>)

    @Update
     suspend fun update(event: EventEntity)

    @Query("DELETE FROM event")
    suspend fun deleteAll()

    @Query("SELECT * FROM event ORDER BY beginTime DESC")
     fun getEvent(): LiveData<List<EventEntity>>

     @Query("SELECT * FROM event WHERE favorite = 1")
     fun getFavoriteEvent(): LiveData<List<EventEntity>>

    @Query("SELECT EXISTS(SELECT * FROM event WHERE event_id = :eventId AND favorite = 1)")
    suspend fun isFavorite(eventId: Int): Boolean

    @Query("UPDATE event SET favorite = :isFav WHERE event_id = :eventId")
    suspend fun updateFavoriteStatus(eventId: Int, isFav: Boolean)

}