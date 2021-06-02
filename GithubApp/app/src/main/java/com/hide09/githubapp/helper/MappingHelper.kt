package com.hide09.githubapp.helper

import android.database.Cursor
import com.hide09.githubapp.db.DatabaseContract
import com.hide09.githubapp.model.User
import com.hide09.githubapp.model.UserDetail

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<User>{
        val favoriteList = ArrayList<User>()

        favoriteCursor?.apply {
            while (moveToNext()){
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_USERNAME))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.COLUMN_PHOTO))
                favoriteList.add(User(username,photo))
            }
        }
        return favoriteList
    }
}