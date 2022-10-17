package com.agil.storyapp

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.agil.storyapp.databinding.ActivityAddStoryBinding
import com.agil.storyapp.viewmodel.AddStoryViewModel
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity(), View.OnFocusChangeListener, View.OnClickListener {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var mUserPreference: UserPreference
    private var isMenuOpen:Boolean = false
    private val myInterpolator = OvershootInterpolator()
    private val restartTranslate = 0.0f
    private val fabScale = 1.1f
    private var imageFile: File? = null
    private var uri: String = ""
    private var desc: String = ""
    private val viewModel: AddStoryViewModel by viewModels()

    companion object{
        val TAG :String = AddStoryActivity::class.java.simpleName
        const val CAMERA_X_RESULT = 200
        private const val IMAGE_URI = "image_uri"
        private const val DESCRIPTION = "desc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        mUserPreference = UserPreference(this)
        if (savedInstanceState != null) {
            updateState(savedInstanceState)
        }

        updateDataStory()
        with(binding){
            fabToCamera.alpha = 0f
            fabToGallery.alpha = 0f
            fabGetPhoto.setOnClickListener(this@AddStoryActivity)
            edAddDescription.onFocusChangeListener = this@AddStoryActivity
            edAddDescription.setOnClickListener(this@AddStoryActivity)
            buttonAdd.setOnClickListener(this@AddStoryActivity)
            root.setOnClickListener(this@AddStoryActivity)
        }
    }

    private fun uploadData() {
        binding.buttonAdd.isEnabled = false
        closeFabMenu()
        binding.fabGetPhoto.isEnabled = false
            var desc = binding.edAddDescription.text.toString()
            val token = mUserPreference.getUser().token
            val image = viewModel.getUrl()
            if (image != null) {
                val file = reduceFileImage(image)
                desc = if (desc == "") "No Description" else desc
                val description = desc.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                if (token != null) {
                    viewModel.postStory(token, imageMultipart, description)
                }
                viewModel.getResponseStatus().observe(this) {
                    val error = it.error
                    val message = it.message
                        if (!message.isEmpty()) showToast(this, message, true)
                        if (!error){
                            finish()
                        }else{
                            binding.buttonAdd.isEnabled = true
                            binding.fabGetPhoto.isEnabled = true
                        }
                }
            } else {
                showToast(this, getString(R.string.no_image), false)
                binding.buttonAdd.isEnabled = true
                binding.fabGetPhoto.isEnabled = true
            }
    }

    private fun updateDataStory() {
        val url = viewModel.getUrl()
        val description = viewModel.getDesc()
        if (url != null){
            setImageView(this,url,binding.ivAddStoryPhoto)
        }
        binding.edAddDescription.setText(description)
    }

    private fun fabPhotoMenu() {
        if (isMenuOpen) closeFabMenu() else openFabMenu()
        with(binding) {
            fabToCamera.setOnClickListener{
                startCameraX()
            }
            fabToGallery.setOnClickListener {
                startGallery()
            }
        }
    }
    private fun openFabMenu() {
        if(!isMenuOpen) {
            isMenuOpen = !isMenuOpen
            with(binding) {
                fabGetPhoto.setImageResource(R.drawable.ic_close)
                fabToCamera.animate().apply {
                    translationX(restartTranslate)
                    translationY(restartTranslate)
                    scaleX(fabScale)
                    scaleY(fabScale)
                    alpha(1f)
                    interpolator = myInterpolator
                    duration = 300
                    start()
                }
                fabToGallery.animate().apply {
                    translationX(restartTranslate)
                    translationY(restartTranslate)
                    scaleX(fabScale)
                    scaleY(fabScale)
                    alpha(1f)
                    interpolator = myInterpolator
                    duration = 300
                    start()
                }
                fabToGallery.isEnabled = isMenuOpen
                fabToCamera.isEnabled = isMenuOpen
            }
        }
    }
    private fun closeFabMenu() {
        if (isMenuOpen) {
            isMenuOpen = !isMenuOpen
            with(binding) {
                fabGetPhoto.setImageResource(R.drawable.ic_add_photo)
                fabToCamera.animate().apply {
                    translationX(10f)
                    translationY(58f)
                    scaleX(1f)
                    scaleY(1f)
                    alpha(0f)
                    interpolator = myInterpolator
                    duration = 300
                    start()
                }
                fabToGallery.animate().apply {
                    translationX(60f)
                    translationY(-4f)
                    scaleX(1f)
                    scaleY(1f)
                    alpha(0f)
                    interpolator = myInterpolator
                    duration = 300
                    start()
                }
                fabToGallery.isEnabled = isMenuOpen
                fabToCamera.isEnabled = isMenuOpen
            }
        }
    }

    private fun startCameraX() {
        closeFabMenu()
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private fun startGallery() {
        closeFabMenu()
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == CAMERA_X_RESULT){
            val myFile = it.data?.getSerializableExtra("picture") as File
            val result = BitmapFactory.decodeFile(myFile.path)
            viewModel.setImageFile(myFile)
            uri = myFile.path
            imageFile = myFile
            setImageView(this,result,binding.ivAddStoryPhoto)
        }
    }
    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == RESULT_OK){
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            val result = BitmapFactory.decodeFile(myFile.path)
            viewModel.setImageFile(myFile)
            uri = myFile.path
            imageFile = myFile
            setImageView(this,result,binding.ivAddStoryPhoto)
        }
    }

    private fun setImageView(context: Context, uri: Any, imageView: ImageView) {
        Glide.with(context)
            .load(uri)
            .into(imageView)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (!hasFocus){
            when(view?.id) {
                binding.edAddDescription.id -> {
                    val edDesc = binding.edAddDescription.text
                    viewModel.setDesc(edDesc.toString())
                }
            }
        }
        closeFabMenu()
    }

    private fun showToast(context: Context,text:String,isLong:Boolean) {
        if (isLong) Toast.makeText(context,text,Toast.LENGTH_LONG).show() else
            Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
    }

    private fun updateState(savedInstanceState: Bundle) {
        val url = savedInstanceState.getString(IMAGE_URI) as String
        val description = savedInstanceState.getString(DESCRIPTION) as String
        if (url != "") {
            setImageView(this, url, binding.ivAddStoryPhoto)
        }
        binding.edAddDescription.setText(description)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(IMAGE_URI, uri)
        outState.putString(DESCRIPTION, desc)
        super.onSaveInstanceState(outState)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            binding.buttonAdd.id -> uploadData()
            binding.fabGetPhoto.id -> fabPhotoMenu()
            binding.edAddDescription.id -> closeFabMenu()
            binding.root.id -> closeFabMenu()

        }
    }
}