package com.agil.androidintermediate.home

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    //jumlah menu
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            //tambahkan pilihan menu
            0 -> fragment = AdvanceUiFragment()
            1 -> fragment = AnimationFragment()
        }
        return fragment as Fragment
    }
}