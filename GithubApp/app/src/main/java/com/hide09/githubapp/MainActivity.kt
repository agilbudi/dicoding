package com.hide09.githubapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
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

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val searchView = binding.svSearch
        userVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        userSearchVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserSearchViewModel::class.java)

        showLoading(true)
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    userSearchVM.setUserSearch(query!!)
                    searchView.clearFocus()
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean = runBlocking{
                        if (TextUtils.isEmpty(newText)){
                            showAllUsers()
                        }else{
                            showLoading(true)
                            launch {
                                delay(300)
                                userSearchVM.setUserSearch(newText)
                            }
                    }
                    return@runBlocking false
                }
            })
        userSearchVM.getUserSearch().observe(this, {userItems ->
            if (userItems != null){
                userAdapter.updateUsers(userItems)
                showLoading(false)
            }else{
                Log.e(TAG, "userItems is $userItems")
            }
        })
        if(userSearchVM.getUserSearch().value == null){
            showAllUsers()
        }

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
            R.id.menu_setting -> {
                supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()
            }
            R.id.menu_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.menu_theme_dark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            R.id.menu_theme_light -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            R.id.menu_exit -> finish()
            R.id.menu_favorite -> startActivity(Intent(this, FavoriteActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAllUsers() {
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