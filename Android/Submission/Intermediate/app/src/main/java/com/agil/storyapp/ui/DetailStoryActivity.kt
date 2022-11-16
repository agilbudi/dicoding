package com.agil.storyapp.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.agil.storyapp.R
import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.databinding.ActivityDetailStoryBinding
import com.agil.storyapp.utils.withDateFormat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        supportActionBar?.hide()

        updateDetail(this)

        if (imageUrl != "") {
        binding.ivDetailPhoto.setOnClickListener { startImagePreview(imageUrl) }
        }
    }

    @Suppress("DEPRECATION")
    private fun updateDetail(detailStoryActivity: DetailStoryActivity) {
        val myStory: StoryEntity = intent.getParcelableExtra<StoryEntity>(EXTRA_STORY) as StoryEntity
        imageUrl = myStory.photoUrl
        val titleColor = ContextCompat.getColor(this@DetailStoryActivity, R.color.white)
        val toolbarTextColor = ContextCompat.getColor(this@DetailStoryActivity, R.color.teal_200)
        with(binding) {
            tvDetailDescription.text = myStory.description
            collapsingToolbar.title = myStory.name
            collapsingToolbar.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD)
            collapsingToolbar.setExpandedTitleColor(titleColor)
            toolbar.setTitleTextColor(toolbarTextColor)
            tvDetailDate.text = myStory.createdAt.withDateFormat()
            Glide.with(detailStoryActivity)
                .load(imageUrl)
                .apply(RequestOptions().centerCrop())
                .into(ivDetailPhoto)
        }
    }

    private fun startImagePreview(imageUri: String) {
        val intent = Intent(this, ImageViewerActivity::class.java)
        intent.putExtra(ImageViewerActivity.EXTRA_URI, imageUri)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}