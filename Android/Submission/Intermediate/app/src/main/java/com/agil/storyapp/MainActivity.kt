package com.agil.storyapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.agil.storyapp.adapter.StoryListAdapter
import com.agil.storyapp.databinding.ActivityMainBinding
import com.agil.storyapp.model.Story
import com.agil.storyapp.model.User
import com.agil.storyapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var user: User
    private val viewModel: MainViewModel by viewModels()
    private val storyAdapter = StoryListAdapter()

    companion object{
        val TAG: String = MainActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mUserPreference = UserPreference(this)
        user = mUserPreference.getUser()
        if (user.token.isNullOrEmpty()){
            startActivity(Intent(this, AuthenticationActivity::class.java))
            finish()
        }else {
            updateListStory(user)
        }

        binding.rvStory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = storyAdapter
        }

        binding.srStory.setOnRefreshListener {
            updateListStory(user)
        }
        storyAdapter.setOnItemClickCallback(object : StoryListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Story) {
                selectedItem(data)
            }
        })
        binding.fabAddStory.setOnClickListener{
            setAddStoryButton()
        }
    }

    private fun setAddStoryButton() {
        startActivity(Intent(this, AddStoryActivity::class.java))
    }

    private fun updateListStory(user: User) {
        binding.srStory.isRefreshing = true
        viewModel.setAllStory(user.token.toString())
        viewModel.getAllStory().observe(this){ item ->
            if (item != null) {
                storyAdapter.updateStory(item)
                binding.srStory.isRefreshing = false
            }else{
                viewModel.getResponseStory().observe(this){
                    if (it != null) {
                        showToast(this,it.message,true)
                    }
                }
            }
        }
    }

    private fun selectedItem(data: Story) {
        val intent = Intent(this, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.EXTRA_STORY, data)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_logout ->{
                val removeState = mUserPreference.delUser()
                if (removeState){
                    startActivity(Intent(this, AuthenticationActivity::class.java))
                    finish()
                    true
                }else{
                    showToast(this, "Logout failed", false)
                    true
                }
            }
            R.id.menu_exit ->{
                finish()
                true
            }
            else -> true
        }
    }

    private fun showToast(context: Context, text:String, isLong:Boolean) {
        if (isLong) Toast.makeText(context,text,Toast.LENGTH_LONG).show() else
            Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        updateListStory(user)
    }
}