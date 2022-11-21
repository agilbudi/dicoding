package com.agil.storyapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.agil.storyapp.adapter.StoryPagingAdapter
import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.data.repository.Result
import com.agil.storyapp.databinding.ActivityMainBinding
import com.agil.storyapp.model.User
import com.agil.storyapp.ui.AddStoryActivity
import com.agil.storyapp.ui.AuthenticationActivity
import com.agil.storyapp.ui.DetailStoryActivity
import com.agil.storyapp.ui.MapStoryActivity
import com.agil.storyapp.ui.viewmodel.MainViewModel
import com.agil.storyapp.ui.viewmodel.MainViewModelFactory
import com.agil.storyapp.utils.UserPreference

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var user: User
    private lateinit var factory: MainViewModelFactory
    private val viewModel: MainViewModel by viewModels {factory}
    private val storyPagingAdapter = StoryPagingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "List Story"
        factory = MainViewModelFactory.getInstance(this)
        mUserPreference = UserPreference(this)
        user = mUserPreference.getUser()
        updateState()

        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = storyPagingAdapter
        }
        updateListStory(user)
        storyPagingAdapter.setOnItemClickCallback(object : StoryPagingAdapter.OnItemClickCallback{
            override fun onItemClicked(data: StoryEntity) {
                selectedItem(data)
            }
        })
        binding.srStory.setOnRefreshListener { updateListStory(user) }
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

    }

    private fun updateState() {
        val firstActivity = mUserPreference.firstActivity()
        if (user.token.isEmpty()) {
            startActivity(Intent(this, AuthenticationActivity::class.java))
            finish()
        }else{
            if (firstActivity != TAG){
                startActivity(Intent(this, MapStoryActivity::class.java))
                finish()
            }
            updateListStory(user)
        }
    }

    private fun updateListStory(user: User) {
        viewModel.story(user.token).observe(this) { result ->
            when(result){
                is Result.Success ->{
                        storyPagingAdapter.submitData(lifecycle, result.data)
                    binding.srStory.isRefreshing = false
                    }
                is Result.Error ->{
                    Toast.makeText(this, result.error,Toast.LENGTH_SHORT).show()
                    binding.srStory.isRefreshing = false
                }
                is Result.Loading -> binding.srStory.isRefreshing = true
            }
        }
    }

    private fun selectedItem(data: StoryEntity) {
        val intent = Intent(this, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.EXTRA_STORY, data)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_list_story, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_maps_story -> {
                mUserPreference.setMainActivity(MapStoryActivity.TAG)
                val intent = Intent(this, MapStoryActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
                true
            }
            R.id.menu_logout -> {
                val removeState = mUserPreference.delUser()
                if (removeState) {
                    startActivity(Intent(this, AuthenticationActivity::class.java))
                    finish()
                    true
                } else {
                    Toast.makeText(this, "Logout failed", Toast.LENGTH_LONG).show()
                    true
                }
            }
            R.id.menu_exit -> {
                finish()
                true
            }
            else -> true
        }
    }

    override fun onResume() {
        super.onResume()
        updateListStory(user)
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}