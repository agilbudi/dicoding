package com.hide09.githubapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.hide09.githubapp.db.DatabaseContract.AUTHORY
import com.hide09.githubapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.hide09.githubapp.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.hide09.githubapp.db.FavoriteHelper

class GithubProvider : ContentProvider() {
    companion object{
        private const val GITHUB = 1
        private const val GITHUB_USERNAME = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper

        init {
            sUriMatcher.addURI(AUTHORY, TABLE_NAME, GITHUB)
            sUriMatcher.addURI(AUTHORY, "$TABLE_NAME/*", GITHUB_USERNAME)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when(GITHUB_USERNAME){
            sUriMatcher.match(uri) -> favoriteHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when(GITHUB){
            sUriMatcher.match(uri) -> favoriteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        Log.e("URI", uri.toString())
        return when(sUriMatcher.match(uri)){
            GITHUB -> favoriteHelper.queryAll()
            GITHUB_USERNAME -> {
                favoriteHelper.queryByUsername(uri.lastPathSegment.toString())
            }
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when(GITHUB_USERNAME){
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment, values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }
}