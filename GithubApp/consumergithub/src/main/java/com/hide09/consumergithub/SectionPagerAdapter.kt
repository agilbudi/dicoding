package com.hide09.consumergithub

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hide09.consumergithub.tablayout.DetailFollowersFragment
import com.hide09.consumergithub.tablayout.DetailFollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    var username: String? = null
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = DetailFollowersFragment.newInstance(username!!)
            1 -> fragment = DetailFollowingFragment.newInstance(username!!)
        }
        return fragment as Fragment
    }
}