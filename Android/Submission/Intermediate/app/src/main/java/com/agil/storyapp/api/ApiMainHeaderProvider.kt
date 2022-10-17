package com.agil.storyapp.api

import android.provider.MediaStore.Video.VideoColumns.LANGUAGE
import com.agil.storyapp.BuildConfig

class ApiMainHeaderProvider {
    fun getUploadAuthenticatedHeaders(accessToken: String): UploadStoryHeaders =
        UploadStoryHeaders().apply {
            put(AUTHORIZATION, getBearer(accessToken))
        }

    fun getAuthenticatedHeaders(accessToken: String): AuthenticatedHeaders =
        AuthenticatedHeaders().apply {
            putAll(getDefaultHeaders())
            put(AUTHORIZATION, getBearer(accessToken))
        }

    private fun getDefaultHeaders() = mapOf(
        ACCEPT_LANGUAGE to LANGUAGE,
        HEADER_ACCEPT to "application/json",
        USER_AGENT to BuildConfig.VERSION_NAME,
    )

    companion object {
        private const val ACCEPT_LANGUAGE = "Accept-Language"
        private const val USER_AGENT = "User-Agent"
        private const val AUTHORIZATION = "Authorization"
        private const val HEADER_ACCEPT = "Accept"

        private fun getBearer(accessToken: String) = "Bearer $accessToken"
    }
}

open class ApiMainHeaders : HashMap<String, String>()
class AuthenticatedHeaders : ApiMainHeaders()
class UploadStoryHeaders : ApiMainHeaders()