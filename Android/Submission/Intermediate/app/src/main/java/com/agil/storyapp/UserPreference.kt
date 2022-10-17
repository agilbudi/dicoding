package com.agil.storyapp

import android.content.Context
import com.agil.storyapp.model.User

internal class UserPreference (context: Context){
    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val enhancer = 1

    companion object{
        private const val PREF_NAME = "user_pref"
        private const val TOKEN = "token"
        private const val GRANT_CAMERA = "grant_camera"
    }

    fun setGrant() {
        val temp = enhancer+getGrant()
        val editor = preferences.edit()
        editor.putInt(GRANT_CAMERA, temp)
        editor.apply()
    }
    fun setUser(token: User){
        val editor = preferences.edit()
        editor.putString(TOKEN, token.token)
        editor.apply()
    }

    fun getGrant(): Int {
        return preferences.getInt(GRANT_CAMERA, 0)
    }
    fun getUser(): User{
        val model = User()
        model.token = preferences.getString(TOKEN, "")
        return model
    }

    fun delUser(): Boolean{
        return if (preferences.all != null) {
            preferences.edit().clear().apply()
            true
        }else{
            false
        }
    }
}