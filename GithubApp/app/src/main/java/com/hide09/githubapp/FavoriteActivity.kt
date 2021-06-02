package com.hide09.githubapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hide09.githubapp.adapter.UserListAdapter
import com.hide09.githubapp.databinding.ActivityFavoriteBinding
import com.hide09.githubapp.db.FavoriteHelper
import com.hide09.githubapp.helper.MappingHelper
import com.hide09.githubapp.model.User
import com.hide09.githubapp.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var userVM: UserViewModel
    private lateinit var favoriteHelper: FavoriteHelper
    private val userAdapter = UserListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite_users)
        userVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        GlobalScope.launch(Dispatchers.Main) {
            favoriteHelper = FavoriteHelper.getInstance(applicationContext)
            showLoading(true)
            binding.rvUsersFavorite.apply {
                hasFixedSize()
                layoutManager = LinearLayoutManager(context)
                adapter = userAdapter
            }
            favoriteHelper.open()
            val result = async(Dispatchers.IO){
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val data = result.await()
            showFavorite(data)
            userAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback{
                override fun onItemClicked(data: User) {
                    selectedItem(data)
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
            R.id.menu_theme_dark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            R.id.menu_theme_light -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    private fun showFavorite(data: ArrayList<User>) {
        userAdapter.updateUsers(data)
        favoriteHelper.close()
        showLoading(false)
    }

    private fun selectedItem(user: User){
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USER, user.username)
        startActivity(intent)
        finish()
    }

    private fun showLoading(status: Boolean) {
        if (status){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }

    }
}