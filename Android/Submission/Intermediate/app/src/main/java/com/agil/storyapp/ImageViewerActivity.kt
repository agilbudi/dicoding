package com.agil.storyapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.agil.storyapp.databinding.ActivityImageViewerBinding
import com.bumptech.glide.Glide
import kotlin.math.max
import kotlin.math.min


class ImageViewerActivity : AppCompatActivity() {
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    companion object {
        private var mScaleFactor = 1.0f
        const val EXTRA_URI = "extra_uri"
        @SuppressLint("StaticFieldLeak")
        private lateinit var binding: ActivityImageViewerBinding
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI()

        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        updateDataImage()

    }
    private fun updateDataImage() {
        val uri = intent.getStringExtra(EXTRA_URI) as String
        setImageView(this, uri, binding.ivImageViewer)
    }

    private fun setImageView(context: Context, uri: Any, imageView: ImageView) {
        Log.d("CAMERA-VIEW", uri.toString())
        Glide.with(context)
            .load(uri)
            .into(imageView)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = max(0.1f, min(mScaleFactor, 10.0f))

            binding.ivImageViewer.scaleX = mScaleFactor
            binding.ivImageViewer.scaleY = mScaleFactor
            return true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}