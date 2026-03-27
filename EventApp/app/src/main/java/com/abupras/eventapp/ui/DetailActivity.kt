package com.abupras.eventapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.abupras.eventapp.R
import com.abupras.eventapp.data.response.DetailEventResponse
import com.abupras.eventapp.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailViewModel::class.java]
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra(EVENTS, 0)
        detailViewModel.getDetailEvent(id)
        detailViewModel.isLoading.observe(this){
            showLoading(it)
        }
        detailViewModel.detailEvent.observe(this) { detail ->
            if (detail != null) {
                showData(detail)
            }
        }
        binding.btnDetailRetry.setOnClickListener {
            detailViewModel.getDetailEvent(id)
        }
        binding.toolbarDetail.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun showData(detail : DetailEventResponse){
        val glide = Glide.with(this@DetailActivity)
        val event = detail.event
        if (detail.error) {
            with(binding){
                tvDetailName.error
                tvDetailOwner.error
                tvDetailTime.error
                tvDetailQuota.error
                tvDetailDescription.error
                collapsingToolbarDetail.title = "No Data"
                tvDetailName.text = event.name
                tvDetailOwner.text = event.ownerName
                tvDetailTime.text = event.beginTime
                tvDetailQuota.text = "Sisa Kuota: ${event.quota-event.registrants}"
                glide.load(getDrawable(R.drawable.icon_no_image_24))
                    .fitCenter()
                    .into(binding.ivDetailImage)
                btnDetailRetry.visibility = View.VISIBLE
                tvDetailDescription.text = detail.message
                fabBrowse.visibility = View.GONE
            }
        } else {
            with(binding) {
                btnDetailRetry.visibility = View.GONE
                fabBrowse.visibility = View.VISIBLE
                collapsingToolbarDetail.title = event.ownerName
                tvDetailName.text = event.name
                tvDetailOwner.text = event.ownerName
                tvDetailTime.text = event.beginTime
                tvDetailQuota.text = "Sisa Kuota: ${event.quota-event.registrants}"
                tvDetailDescription.text = descFromHTML(event.description)
                glide.load(event.mediaCover)
                    .into(ivDetailImage)
                fabBrowse.setOnClickListener {
                    if (event.link.isNotEmpty()){
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@DetailActivity, "Link tidak tersedia", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun descFromHTML(desc: String) = HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY)
    private fun showLoading(isLoading: Boolean){
        binding.pgBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object{
        const val EVENTS = "EVENTS"
    }
}