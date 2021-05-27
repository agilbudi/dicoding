package com.hide09.githubapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hide09.githubapp.adapter.UserListAdapter
import com.hide09.githubapp.databinding.ActivityMainBinding
import com.hide09.githubapp.model.User
import com.hide09.githubapp.viewmodel.UserSearchViewModel
import com.hide09.githubapp.viewmodel.UserViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userVM: UserViewModel
    private lateinit var userSearchVM: UserSearchViewModel
    private val userAdapter = UserListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val searchView = binding.svSearch

        showLoading(true)
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
        showAllUsers()
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    showSearch(query)
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    runBlocking {
                        launch {
                            delay(300)
                            Log.d(MainActivity::class.java.simpleName, newText.toString())
                            if (newText.isNullOrEmpty()){
                                showAllUsers()
                            }else{
                                showSearch(newText)
                            }
                        }
                    }
                    return false
                }
            })

        userAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                selectedItem(data)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_setting -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.menu_theme_dark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            R.id.menu_theme_light -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            R.id.menu_exit -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSearch(username: String?) {
        showLoading(true)
        userSearchVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserSearchViewModel::class.java)
        userSearchVM.setUserSearch(username!!)
        userSearchVM.getUserSearch().observe(this, {userItems ->
            if (userItems != null){
                userAdapter.updateUsers(userItems)
                showLoading(false)
            }else{
                Log.d(MainActivity::class.java.simpleName, "userItems = " + userItems.toString())
            }
        })
    }

    private fun showAllUsers() {
        userVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        userVM.setUsers()
        userVM.getUsers().observe(this, {userItems ->
            if (userItems != null){
                userAdapter.updateUsers(userItems)
                showLoading(false)
            }
        })
    }

    private fun selectedItem(user: User){
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
}