package com.abupras.eventapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.abupras.eventapp.R
import com.abupras.eventapp.data.response.DetailEventResponse
import com.abupras.eventapp.databinding.ActivityDetailBinding
import com.abupras.eventapp.utils.getPalette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

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
                descFromHTML(event.description, tvDetailDescription)
                glide.asBitmap().load(event.imageLogo)
                    .listener(object : RequestListener<Bitmap>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>,
                            isFirstResource: Boolean,
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            model: Any,
                            target: Target<Bitmap>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean,
                        ): Boolean {
                            val palette = getPalette(resource).vibrantSwatch
                            val color = palette?.rgb ?: COLOR_DEFAULT
                            val textColor = palette?.titleTextColor ?: Color.BLACK
                            collapsingToolbarDetail.setContentScrimColor(color)
                            collapsingToolbarDetail.setCollapsedTitleTextColor(textColor)
                            collapsingToolbarDetail.setExpandedTitleColor(Color.TRANSPARENT)
                            return false
                        }
                    })
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

    private fun descFromHTML(desc: String, textView: android.widget.TextView) {
        val imageGetter = Html.ImageGetter { source ->
            val drawable = android.graphics.drawable.LevelListDrawable()

            // Gunakan Glide untuk memuat gambar dari URL (source)
            Glide.with(this@DetailActivity)
                .asBitmap()
                .load(source)
                .into(object : com.bumptech.glide.request.target.CustomTarget<android.graphics.Bitmap>() {
                    override fun onResourceReady(
                        resource: android.graphics.Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in android.graphics.Bitmap>?,
                    ) {
                        val bitmapDrawable = android.graphics.drawable.BitmapDrawable(resources, resource)
                        drawable.addLevel(1, 1, bitmapDrawable)

                        // Mengatur ukuran gambar agar sesuai dengan aspek rasio
                        val width = textView.width // Sesuaikan dengan lebar TextView
                        val aspectRatio = resource.height.toDouble() / resource.width.toDouble()
                        val height = (width * aspectRatio).toInt()

                        drawable.setBounds(0, 0, width, height)
                        drawable.level = 1

                        // Refresh TextView agar gambar muncul
                        textView.text = textView.text
                    }

                    override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {}
                })
            drawable
        }

        // Terapkan ke TextView
        textView.text = HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY, imageGetter, null)
    }
    private fun showLoading(isLoading: Boolean){
        binding.pgBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object{
        private const val COLOR_DEFAULT = Color.LTGRAY
        const val EVENTS = "EVENTS"
    }
}