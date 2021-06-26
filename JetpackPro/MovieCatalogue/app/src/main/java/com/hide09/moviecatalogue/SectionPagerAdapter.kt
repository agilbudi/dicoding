package com.hide09.moviecatalogue

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hide09.moviecatalogue.tablayout.MoviesFragment
import com.hide09.moviecatalogue.tablayout.TvShowFragment

class SectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    val type: String? = null
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = MoviesFragment()
            1 -> fragment = TvShowFragment()
        }
        return fragment as Fragment
    }

}
