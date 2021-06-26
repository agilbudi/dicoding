package com.hide09.moviecatalogue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hide09.moviecatalogue.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object{
        private val TAB_TITLES = intArrayOf(
            R.string.tab_movies,
            R.string.tab_tv_show
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabLayout

        viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(tabs, viewPager){tab, position->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}