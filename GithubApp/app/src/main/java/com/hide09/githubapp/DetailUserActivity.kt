package com.hide09.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hide09.githubapp.databinding.ActivityDetailUserBinding
import com.hide09.githubapp.viewmodel.UserDetailViewModel

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    lateinit var userDetailVM: UserDetailViewModel

    companion object{
        const val EXTRA_USER = "extra_user"
        private val TAB_TITLES = intArrayOf(
                R.string.tab_followers,
                R.string.tab_following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabLayout
        val username = intent.getStringExtra(EXTRA_USER)
        setData(username)
        sectionPagerAdapter.username = username

        viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(tabs, viewPager){tab, position->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setData(username: String?) {
        userDetailVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserDetailViewModel::class.java)
        userDetailVM.setDataUserDetail(username)
        userDetailVM.getUserDetail().observe(this, {items ->
            with(binding){
                Glide.with(this@DetailUserActivity)
                        .load(items.detailFoto)
                        .apply {
                            RequestOptions().centerCrop()
                            RequestOptions().override(130, 130)
                        }
                        .into(civDetailUser)
                tvDetailUsername.text = items.detailUserName
                tvDetailNama.text = items.detailNama
                tvDetailRepo.text = items.detailRepo
                tvDetailFollowing.text = items.detailFollowing
                tvDetailFollowers.text = items.detailFollowers
                supportActionBar?.title = items.detailNama
            }
        })
    }

}