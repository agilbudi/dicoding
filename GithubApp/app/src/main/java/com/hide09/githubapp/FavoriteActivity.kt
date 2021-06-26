package com.hide09.githubapp

import android.content.Intent
import android.database.ContentObserver
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
import com.hide09.githubapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hide09.githubapp.adapter.FavoriteListAdapter
import com.hide09.githubapp.databinding.ActivityFavoriteBinding
import com.hide09.githubapp.entity.Favorite
import com.hide09.githubapp.helper.MappingHelper
import com.hide09.githubapp.viewmodel.UserFavoriteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteVM: UserFavoriteViewModel
    private val userAdapter = FavoriteListAdapter()
    companion object{
        val TAG: String = FavoriteActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite_users)
        favoriteVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserFavoriteViewModel::class.java)
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        binding.rvUsersFavorite.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }

        loadData()
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadData()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        userAdapter.setOnItemClickCallback(object : FavoriteListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Favorite) {
                selectedItem(data)
            }
        })
    }

    private fun loadData() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(true)
            val data = deferred.await()
            showLoading(false)
            favoriteVM.setFavorite(data)
                favoriteVM.getFavorite().observe(this@FavoriteActivity, { items ->
                    if (items.size > 0) {
                        userAdapter.updateFavorite(items)
                    } else {
                        userAdapter.updateFavorite(items)
                        Toast.makeText(this@FavoriteActivity, getString(R.string.message_no_user_favorite), Toast.LENGTH_LONG).show()
                    }
                })
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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun selectedItem(user: Favorite){
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USER, user.username)
        startActivity(intent)
    }

    private fun showLoading(status: Boolean) {
        if (status){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast( message: String?, durationLong: Boolean) {
        if (durationLong) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

}