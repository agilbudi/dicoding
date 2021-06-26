package com.hide09.consumergithub.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORY = "com.hide09.githubapp"
    const val SCHEME = "content"
    internal class FavoriteColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite"
            const val COLUMN_ID = "_id"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_PHOTO = "photo"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}