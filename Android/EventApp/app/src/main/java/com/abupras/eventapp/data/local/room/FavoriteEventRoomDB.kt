package com.abupras.eventapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.abupras.eventapp.data.local.entitiy.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = false)
abstract class FavoriteEventRoomDB : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        private const val DB_NAME = "event.db"

        @Volatile
        private var INSTANCE: FavoriteEventRoomDB? = null
//        private val MIGRATION_1_2 = object : Migration(1,2){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE IF NOT EXISTS `favorite` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `event_id` INTEGER NOT NULL, `favorite` INTEGER NOT NULL)")
//            }
//        }

        fun getInstance(context: Context): FavoriteEventRoomDB =
            INSTANCE ?: synchronized(FavoriteEventRoomDB::class) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteEventRoomDB::class.java, DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
    }
}