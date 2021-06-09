package com.hide09.githubapp

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class MyPreferenceFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}