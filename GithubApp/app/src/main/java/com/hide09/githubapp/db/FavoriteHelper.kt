package com.hide09.githubapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.hide09.githubapp.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.hide09.githubapp.db.DatabaseContract.FavoriteColumns.Companion.COLUMN_ID
import com.hide09.githubapp.db.DatabaseContract.FavoriteColumns.Companion.COLUMN_USERNAME

class FavoriteHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME

        private var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context): FavoriteHelper= INSTANCE ?: synchronized(this){
            INSTANCE?: FavoriteHelper(context)
        }
    }

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }
    fun close(){
        database.close()
        if (database.isOpen){
            database.close()
        }
    }

    fun queryAll(): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_ID ASC")
    }

    fun queryByUsername(username: String): Cursor{
        return database.query(
            DATABASE_TABLE,
            null,
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?): Long{
        return database.insert(DATABASE_TABLE,null,values)
    }

    fun update(id: String?, values: ContentValues?): Int{
        return database.update(DATABASE_TABLE,values,"$COLUMN_ID = ?", arrayOf(id))
    }

    fun deleteByUsername(username: String): Int{
        return database.delete(DATABASE_TABLE,"$COLUMN_USERNAME = '$username'", null)
    }
}