package com.agil.storyapp.ui

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.agil.storyapp.R
import com.agil.storyapp.data.repository.Result
import com.agil.storyapp.databinding.ActivityAddStoryBinding
import com.agil.storyapp.ui.viewmodel.AddStoryViewModel
import com.agil.storyapp.ui.viewmodel.AddStoryViewModelFactory
import com.agil.storyapp.utils.*
import com.bumptech.glide.Glide
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class AddStoryActivity : AppCompatActivity(), View.OnFocusChangeListener, View.OnClickListener {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var mUserPreference: UserPreference
    private var isMenuOpen: Boolean = false
    private val myInterpolator = OvershootInterpolator()
    private val restartTranslate = 0.0f
    private val fabScale = 1.1f
    private var lat: Float? = null
    private var lng: Float? = null
    private var imagePath: String = ""
    private var desc: String = ""
    private lateinit var factory: AddStoryViewModelFactory
    private val viewModel: AddStoryViewModel by viewModels { factory }
    private var isLocationEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        factory = AddStoryViewModelFactory.getInstance()
        setContentView(binding.root)
        supportActionBar?.hide()
        mUserPreference = UserPreference(this)
        if (savedInstanceState != null) {
            updateState(savedInstanceState)
        }

        updateDataStory()
        with(binding) {
            fabToCamera.alpha = 0f
            fabToGallery.alpha = 0f
            fabGetPhoto.setOnClickListener(this@AddStoryActivity)
            edAddDescription.onFocusChangeListener = this@AddStoryActivity
            edAddDescription.setOnClickListener(this@AddStoryActivity)
            buttonAdd.setOnClickListener(this@AddStoryActivity)
            rbAddLocation.setOnClickListener(this@AddStoryActivity)
            root.setOnClickListener(this@AddStoryActivity)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.buttonAdd.id -> uploadData()
            binding.fabGetPhoto.id -> fabPhotoMenu()
            binding.edAddDescription.id -> closeFabMenu()
            binding.root.id -> closeFabMenu()
            binding.rbAddLocation.id -> enableLocation()
        }
    }

    private fun enableLocation() {
        val alertDialog = AlertDialog.Builder(this).setTitle("GPS required")
        with(alertDialog){
            setMessage("These are required to use GPS, " +
                    "\nPlease 'Turn On' to get your location.")
            setPositiveButton("Open Setting"){_,_ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            setNegativeButton("Cancel"){ dialogInterface,_ ->
                dialogInterface.cancel()
            }
        }
        if (!isLocationEnable){
            if (checkMapPermissions(this)){
                if (isLocationEnabled(this)){
                    checkedRabLocation()
                }else{
                    alertDialog.show()
                }
            }else{
                requestMapPermission(this)
            }
        } else{
            unCheckedRabLocation()
        }
        binding.rbAddLocation.isChecked = isLocationEnable
    }

    private fun checkedRabLocation() {
        showLoading(true)
        if (!isLocationEnable) {
            val location = runBlocking { getCurrentLocation(this@AddStoryActivity) }
            if (location != null) {
                mUserPreference.setLocation(location)
                lat = location.latitude.toFloat()
                lng = location.longitude.toFloat()
            }
            isLocationEnable = !isLocationEnable
            showLoading(false)
        }
    }
    private fun unCheckedRabLocation(){
        if (isLocationEnable) {
            isLocationEnable = !isLocationEnable
        }
    }


    private fun uploadData() {
        closeFabMenu()
        val desc = binding.edAddDescription.text.toString()
        val user = mUserPreference.getUser()
        val token = user.token
        val image = if (imagePath != "") File(imagePath) else null
        var latitude = if (user.lat != 0F) user.lat else null
        var longitude = if (user.lon != 0F) user.lon else null
        if (image != null) {
            if (desc != "") {
                val file = reduceFileImage(image)
                val description = desc.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                if (!isLocationEnable){
                    latitude = null
                    longitude = null
                }
                viewModel.uploadStory(token, imageMultipart, description, latitude, longitude).observe(this){ result ->
                    if (result != null) {
                        when(result){
                            is Result.Success -> {
                                showToast(this, result.data.message, false)
                                finish()
                            }
                            is Result.Error ->{
                                showToast(this, result.error, true)
                                showLoading(false)
                            }
                            is Result.Loading -> showLoading(true)
                        }
                    }
                }
            } else {
                showToast(this, "please insert a description", false)
                showLoading(false)
            }
        } else {
            showToast(this, getString(R.string.no_image), false)
            showLoading(false)
        }
    }

    private fun fabPhotoMenu() {
        if (isMenuOpen) closeFabMenu() else openFabMenu()
        with(binding) {
            fabToCamera.setOnClickListener {
                startCameraX()
            }
            fabToGallery.setOnClickListener {
                startGallery()
            }
        }
    }
    private fun openFabMenu() {
        if (!isMenuOpen) {
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

    @Suppress("DEPRECATION")
    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val myFile = it.data?.getSerializableExtra("picture") as File
                val result = BitmapFactory.decodeFile(myFile.path)
                imagePath = myFile.path
                setImageView(this, result, binding.ivAddStoryPhoto)
            }
        }
    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val selectedImg: Uri = it.data?.data as Uri
                val myFile = uriToFile(selectedImg, this)
                val result = BitmapFactory.decodeFile(myFile.path)
                imagePath = myFile.path
                setImageView(this, result, binding.ivAddStoryPhoto)
            }
        }

    private fun updateDataStory() {
        val image = if(imagePath != "") {
            imagePath
        } else  {
            ContextCompat.getDrawable(this, R.drawable.ic_image_default_foreground) as Any
        }
        val user = mUserPreference.getUser()
        val latitude = if (user.lat?.toDouble() != 0.0) user.lat else null
        val longitude = if (user.lon?.toDouble() != 0.0) user.lon else null
        if (latitude != null && longitude != null){
            lat = latitude
            lng = longitude
            isLocationEnable = true
        }
        binding.rbAddLocation.isChecked = isLocationEnable
        setImageView(this, image, binding.ivAddStoryPhoto)
        binding.edAddDescription.setText(desc)
    }

    private fun updateState(savedInstanceState: Bundle) {
        val path = savedInstanceState.getString(IMAGE_PATH) as String
        val description = savedInstanceState.getString(DESCRIPTION) as String
        val locationState = savedInstanceState.getBoolean(RADIO_STATE)
        imagePath = path
        desc = description
        isLocationEnable = locationState
    }

    private fun showLoading(isLoading: Boolean){
        binding.buttonAdd.isEnabled = !isLoading
        binding.fabGetPhoto.isEnabled = !isLoading
        binding.pbAddStory.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(context: Context, text: String, isLong: Boolean) {
        if (isLong) Toast.makeText(context, text, Toast.LENGTH_LONG).show() else
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun setImageView(context: Context, uri: Any, imageView: ImageView) {
        Glide.with(context)
            .load(uri)
            .into(imageView)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            when (view?.id) {
                binding.edAddDescription.id -> {
                    val edDesc = binding.edAddDescription.text.toString()
                    desc = edDesc
                }
            }
        }
        closeFabMenu()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(IMAGE_PATH, imagePath)
        outState.putString(DESCRIPTION, desc)
        outState.putBoolean(RADIO_STATE, isLocationEnable)
        super.onSaveInstanceState(outState)
    }

    companion object {
        val TAG: String = AddStoryActivity::class.java.simpleName
        const val CAMERA_X_RESULT = 200
        private const val IMAGE_PATH = "image_path"
        private const val DESCRIPTION = "desc"
        private const val RADIO_STATE = "radio_state"
    }
}