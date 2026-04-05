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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.abupras.eventapp.R
import com.abupras.eventapp.data.Result
import com.abupras.eventapp.data.local.entitiy.EventEntity
import com.abupras.eventapp.databinding.ActivityDetailBinding
import com.abupras.eventapp.utils.getPalette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: EventViewModel by viewModels {
            factory
        }
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra(EVENTS, 0)
        viewModel.getDetailEvent(id).observe(this) { detail ->
            if (detail != null) {
                showData(detail)
            }
        }
        viewModel.snackbarMessage.observe(this){ mesage ->
            Snackbar.make(binding.fabDetailFavorite, mesage, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.isFavorite.observe(this){
            isFavorite = it
        }
        binding.apply{
            binding.fabDetailFavorite.setOnClickListener {
                toggleFavorite(isFavorite)
                viewModel.setFavorite(id, !isFavorite)
            }

            binding.btnDetailRetry.setOnClickListener {
                viewModel.getDetailEvent(id)
            }
            binding.toolbarDetail.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
    private fun toggleFavorite(state: Boolean) {
            binding.fabDetailFavorite.isActivated = !state
    }
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun showData(detail: Result<EventEntity>){
            val glide = Glide.with(this@DetailActivity)
            when(detail){
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    with(binding) {
                        collapsingToolbarDetail.title = "No Data"
                        collapsingToolbarDetail.setExpandedTitleColor(Color.TRANSPARENT)
                        glide.load(getDrawable(R.drawable.icon_no_image_24))
                            .into(binding.ivDetailImage)
                        tvDetailErrorMessage.text = detail.error
                        btnDetailRetry.visibility = View.VISIBLE
                        linDetailError.visibility = View.VISIBLE
                        fabBrowse.visibility = View.GONE
                        fabDetailFavorite.visibility = View.GONE
                        linDetailBody.visibility = View.GONE
                    }
                }
                is Result.Success -> {
                    showLoading(false)
                    val event = detail.data
                    with(binding) {
                        linDetailBody.visibility = View.VISIBLE
                        fabBrowse.visibility = View.VISIBLE
                        linDetailError.visibility = View.GONE
                        btnDetailRetry.visibility = View.GONE
                        fabDetailFavorite.visibility = View.VISIBLE
                        fabDetailFavorite.isActivated = event.isFavorite
                        isFavorite = event.isFavorite
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
                                    fabDetailFavorite.rippleColor = color
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
    }

    private fun descFromHTML(desc: String, textView: android.widget.TextView) {
        val imageGetter = Html.ImageGetter { source ->
            val drawable = android.graphics.drawable.LevelListDrawable()

            Glide.with(this@DetailActivity)
                .asBitmap()
                .load(source)
                .into(object : com.bumptech.glide.request.target.CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?,
                    ) {
                        val bitmapDrawable = android.graphics.drawable.BitmapDrawable(resources, resource)
                        drawable.addLevel(1, 1, bitmapDrawable)

                        val width = textView.width
                        val aspectRatio = resource.height.toDouble() / resource.width.toDouble()
                        val height = (width * aspectRatio).toInt()

                        drawable.setBounds(0, 0, width, height)
                        drawable.level = 1

                        textView.text = textView.text
                    }

                    override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {}
                })
            drawable
        }

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


