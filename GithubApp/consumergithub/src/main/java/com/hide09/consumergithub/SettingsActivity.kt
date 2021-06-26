package com.hide09.consumergithub

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.hide09.consumergithub.alarm.AlarmReceiver

class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var REMINDER: String
    private lateinit var THEME: String
    private lateinit var time: String
    private lateinit var massage: String
    private val alarmReceiver = AlarmReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this)

        REMINDER = resources.getString(R.string.key_reminder)
        THEME = resources.getString(R.string.key_theme)
        time = "09:00"
        massage = resources.getString(R.string.massage)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when(key){
            THEME -> {
                val prefs = sharedPreferences?.getString(key, "1")
                when(prefs?.toInt()){
                    1 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    2 -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
                Log.e("SETTING", "$key = $prefs")
            }
            REMINDER ->{
                val prefs = sharedPreferences?.getBoolean(key, false)
                if (prefs == true){
                    alarmReceiver.setRepeatingAlarm(this, getString(R.string.app_name), time, massage)
                }else{
                    alarmReceiver.cancelAlarm(this)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}