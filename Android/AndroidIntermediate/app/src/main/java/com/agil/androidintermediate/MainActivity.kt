package com.agil.androidintermediate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.StringRes
import com.agil.androidintermediate.databinding.ActivityMainBinding
import com.agil.androidintermediate.home.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionPageAdapter = SectionsPagerAdapter(this)
        val viewPager = binding.vpMain
        val tabs = binding.tabs

        viewPager.adapter = sectionPageAdapter
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 2f
    }

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.page_advanced_ui,
            R.string.page_animation
        )
    }
}