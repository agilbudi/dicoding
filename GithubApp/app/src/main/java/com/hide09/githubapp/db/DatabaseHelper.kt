package com.hide09.githubapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hide09.githubapp.db.DatabaseContract.FavoriteColumns.Companion.COLUMN_ID
import com.hide09.githubapp.db.DatabaseContract.FavoriteColumns.Companion.COLUMN_PHOTO
import com.hide09.githubapp.db.DatabaseContract.FavoriteColumns.Companion.COLUMN_USERNAME
import com.hide09.githubapp.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "dbgithubapp"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                " (${COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $COLUMN_USERNAME TEXT NOT NULL," +
                " $COLUMN_PHOTO TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}