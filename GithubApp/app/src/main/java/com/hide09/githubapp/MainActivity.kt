package com.hide09.githubapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hide09.githubapp.adapter.UserListAdapter
import com.hide09.githubapp.databinding.ActivityMainBinding
import com.hide09.githubapp.model.User
import com.hide09.githubapp.viewmodel.UserDetailViewModel
import com.hide09.githubapp.viewmodel.UserSearchViewModel
import com.hide09.githubapp.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var userVM: UserViewModel
    lateinit var userSearchVM: UserSearchViewModel
    private val userAdapter = UserListAdapter()

    companion object{
        private val TAG = MainActivity::class.java.simpleName
    }

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
                    if (newText.isNullOrEmpty()){
                        showAllUsers()
                    }else{
                        showSearch(newText)
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

    private fun showSearch(username: String?) {
        showLoading(true)
        userSearchVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserSearchViewModel::class.java)
        userSearchVM.setUserSearch(username)
        userSearchVM.getUserSearch().observe(this, {userItems ->
        if (userItems != null){
            userAdapter.updateUsers(userItems)
            showLoading(false)
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