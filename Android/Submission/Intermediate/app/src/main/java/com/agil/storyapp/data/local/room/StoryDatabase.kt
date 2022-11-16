package com.agil.storyapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.agil.storyapp.data.local.entity.RemoteKeys
import com.agil.storyapp.data.local.entity.StoryEntity

@Database(entities = [StoryEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class StoryDatabase : RoomDatabase(){
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeyDao(): RemoteKeyDao

    companion object{
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoryDatabase {
            if (INSTANCE == null){
                synchronized(StoryDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    StoryDatabase::class.java,"story_db")
                        .build()
                }
            }
            return INSTANCE as StoryDatabase
        }
    }
}