package com.agil.storyapp.utils

import android.content.Context
import android.location.Location
import com.agil.storyapp.model.User

internal class UserPreference(context: Context) {
    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val enhancer = 1

    fun setMainActivity(tag: String){
        val editor = preferences.edit()
        editor.putString(MAIN_ACTIVITY, tag)
        editor.apply()
    }

    fun setGrantCamera() {
        val temp = enhancer + getGrantCamera()
        val editor = preferences.edit()
        editor.putInt(GRANT_CAMERA, temp)
        editor.apply()
    }

    fun setUser(user: User) {
        val editor = preferences.edit()
        editor.putString(TOKEN, user.token)
        editor.putString(UID, user.userId)
        editor.apply()
    }

    fun setLocation(location: Location){
        val editor = preferences.edit()
        val lat = location.latitude
        val lng = location.longitude
        editor.putFloat(LAT, lat.toFloat())
        editor.putFloat(LNG, lng.toFloat())
        editor.apply()
    }

    fun getGrantCamera(): Int {
        return preferences.getInt(GRANT_CAMERA, 0)
    }

    fun firstActivity(): String {
        return preferences.getString(MAIN_ACTIVITY, "").toString()
    }

    fun getUser(): User {
        val model = User()
        val uid = preferences.getString(UID,"")
        val token = preferences.getString(TOKEN, "")
        if (uid != null && token != null){
            model.userId = uid
            model.token = token
        }
        model.lat = preferences.getFloat(LAT, 0F)
        model.lon = preferences.getFloat(LNG, 0F)
        return model
    }

    fun delUser(): Boolean {
        return if (preferences.all != null) {
            preferences.edit().clear().apply()
            true
        } else {
            false
        }
    }

    companion object {
        private const val PREF_NAME = "user_pref"
        private const val TOKEN = "token"
        private const val UID = "uId"
        private const val LAT = "lat"
        private const val LNG = "lng"
        private const val GRANT_CAMERA = "grant_camera"
        private const val MAIN_ACTIVITY = "main_activity"
    }
}