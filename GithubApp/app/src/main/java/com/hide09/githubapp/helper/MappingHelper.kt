package com.hide09.githubapp.helper

import android.database.Cursor
import com.hide09.githubapp.db.DatabaseContract
import com.hide09.githubapp.entity.Favorite

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<Favorite>{
        val favoriteList = ArrayList<Favorite>()

        favoriteCursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_USERNAME))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_PHOTO))
                favoriteList.add(Favorite(username,photo))
            }
        }
        return favoriteList
    }
    fun mapCursorToObject(favoriteCursor: Cursor?): Favorite{
        var favorite = Favorite()
        favoriteCursor?.apply {
            moveToFirst()
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_USERNAME))
            val photo = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_PHOTO))
            favorite = Favorite(username, photo)
        }
        return favorite
    }
}