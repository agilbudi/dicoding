package com.hide09.consumergithub

import android.content.ContentValues
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hide09.consumergithub.databinding.ActivityDetailUserBinding
import com.hide09.consumergithub.db.DatabaseContract
import com.hide09.consumergithub.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.hide09.consumergithub.helper.MappingHelper
import com.hide09.consumergithub.viewmodel.UserDetailViewModel
import kotlinx.coroutines.*
import java.lang.IllegalStateException

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var userDetailVM: UserDetailViewModel
    private lateinit var uriWithUsername: Uri
    private var statusFavorite: Boolean = false
    private val columns = DatabaseContract.FavoriteColumns

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
        userDetailVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserDetailViewModel::class.java)
        val username: String = intent.getStringExtra(EXTRA_USER).toString()
        val sectionPagerAdapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabLayout
        uriWithUsername = Uri.parse("$CONTENT_URI/$username")
        sectionPagerAdapter.username = username
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        showLoading(true)
        if (userDetailVM.getUserDetail().value == null){
            loadAsync(username)
        }
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadAsync(username)
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(tabs, viewPager){tab, position->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        binding.fabFav.setOnClickListener {
            if (!statusFavorite){
                getFavorite()
            }else{
                getUnFavorite()
            }
            setStatusFavorite(statusFavorite)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_secondary, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.menu_setting -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.menu_exit -> onDestroy()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadAsync(username: String) {
        GlobalScope.launch(Dispatchers.Main){
            userDetailVM.setDataUserDetail(username)
            try {
                userDetailVM.getUserDetail().observe(this@DetailUserActivity, {items ->
                    with(binding){
                        Glide.with(this@DetailUserActivity)
                            .load(items.detailPhoto)
                            .apply {
                                RequestOptions().centerCrop()
                                RequestOptions().override(130, 130)
                            }
                            .into(civDetailUser)
                        tvDetailUsername.text = items.detailUserName
                        tvDetailName.text = items.detailName
                        tvDetailRepo.text = items.detailRepo
                        tvDetailFollowings.text = items.detailFollowings
                        tvDetailFollowers.text = items.detailFollowers
                        supportActionBar?.title = items.detailName
                    }
                })
            }catch (e: IllegalStateException){
                Log.e(DetailUserActivity::class.java.simpleName, e.message.toString())
            }

            val deferred = async(Dispatchers.IO){
                val cursor = contentResolver.query(uriWithUsername,null, null,null,null,null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val data = deferred.await()
            showLoading(false)
            statusFavorite = data.size != 0
            setStatusFavorite(statusFavorite)
        }
    }

    private fun getUnFavorite() {
        if (binding.tvDetailUsername.text.isNullOrEmpty()) {
            Toast.makeText(this, "${binding.tvDetailName.text} ${getString(R.string.delete_favorite_failed)}", Toast.LENGTH_SHORT).show()
        }else{
            contentResolver.delete(uriWithUsername,null,null)
            statusFavorite = !statusFavorite
            Toast.makeText(this, "${binding.tvDetailName.text} ${getString(R.string.delete_favorite)}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFavorite() {
        val values = ContentValues()
        userDetailVM.getUserDetail().observe(this,
                { items ->
                    values.put(columns.COLUMN_USERNAME, items.detailUserName)
                    values.put(columns.COLUMN_PHOTO, items.detailPhoto)
                })
        if (binding.tvDetailUsername.text.isNullOrEmpty()){
            Toast.makeText(this, "${binding.tvDetailName.text} ${getString(R.string.add_favorite_failed)}", Toast.LENGTH_SHORT).show()
        }else{
            contentResolver.insert(CONTENT_URI, values)
            statusFavorite = !statusFavorite
            Toast.makeText(this, "${binding.tvDetailName.text} ${getString(R.string.add_favorite)}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if(statusFavorite){
            binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_24)
        }else{
            binding.fabFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            binding.progressBarDetail.visibility = View.VISIBLE
        }else{
            binding.progressBarDetail.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}