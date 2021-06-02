package com.hide09.githubapp

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat


class MyPreferenceFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}