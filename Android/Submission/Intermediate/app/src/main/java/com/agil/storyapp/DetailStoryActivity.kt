package com.agil.storyapp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.agil.storyapp.databinding.ActivityDetailStoryBinding
import com.agil.storyapp.model.Story
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    companion object{
        const val EXTRA_STORY = "extra_story"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        supportActionBar?.hide()

        val uri = updateDetail(this)
        Log.e("URI",uri)
        binding.ivDetailPhoto.setOnClickListener {
            if (uri != "") {
                startImagePreview(uri)
            }
        }
    }

    private fun updateDetail(detailStoryActivity: DetailStoryActivity): String {
        val myStory : Story = intent.getParcelableExtra<Story>(EXTRA_STORY) as Story
        val url = myStory.photoUrl.toString()
        val titleColor = ContextCompat.getColor(this@DetailStoryActivity,R.color.white)
        val toolbarTextColor = ContextCompat.getColor(this@DetailStoryActivity,R.color.teal_200)
        with(binding) {
            tvDetailDescription.text = myStory.description
            collapsingToolbar.title = myStory.name
            collapsingToolbar.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD)
            collapsingToolbar.setExpandedTitleColor(titleColor)
            toolbar.setTitleTextColor(toolbarTextColor)
            Glide.with(detailStoryActivity)
                .load(url)
                .apply(RequestOptions().centerCrop())
                .into(ivDetailPhoto)
        }
        return url
    }
    private fun startImagePreview(imageUri: String) {
        val intent = Intent(this, ImageViewerActivity::class.java)
        intent.putExtra(ImageViewerActivity.EXTRA_URI, imageUri)
        startActivity(intent)
    }
}