package com.hide09.consumergithub

import android.content.Intent
import android.content.SharedPreferences
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
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hide09.consumergithub.adapter.FavoriteListAdapter
import com.hide09.consumergithub.databinding.ActivityMainBinding
import com.hide09.consumergithub.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.hide09.consumergithub.entity.Favorite
import com.hide09.consumergithub.helper.MappingHelper
import com.hide09.consumergithub.viewmodel.UserFavoriteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserFavoriteViewModel
    private var favoriteAdapter = FavoriteListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserFavoriteViewModel::class.java)
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        mySetting(prefs)
        binding.rvUsersFavorite.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }

        dataAsync()
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                dataAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        favoriteAdapter.setOnItemClickCallback(object : FavoriteListAdapter.OnItemClickCallback{
            override fun onItemClicked(item: Favorite) {
                selectedItem(item)
            }
        })
    }

    private fun mySetting(prefs: SharedPreferences) {
        val keyTheme = resources.getString(R.string.key_theme)
        val theme = prefs.getString(keyTheme, "1")
        when(theme?.toInt()){
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.menu_setting -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.menu_exit -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectedItem(item: Favorite) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USER, item.username)
        startActivity(intent)
    }

    private fun dataAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(true)
            val data = deferred.await()
            showLoading(false)
            viewModel.setFavorite(data)
            viewModel.getFavorite().observe(this@MainActivity, {items ->
                if (items.size > 0){
                    favoriteAdapter.updateFavorite(items)
                }else{
                    favoriteAdapter.updateFavorite(items)
                    Toast.makeText(this@MainActivity, getString(R.string.message_no_user_favorite), Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun showLoading(status: Boolean) {
        if (status){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }
}