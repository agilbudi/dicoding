package com.hide09.githubapp.db

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class FavoriteColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite"
            const val COLUMN_ID = "_id"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_PHOTO = "photo"
        }
    }
}