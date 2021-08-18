package com.hide09.androidfundamental.navigationevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hide09.androidfundamental.R

class MainNavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_navigation)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}