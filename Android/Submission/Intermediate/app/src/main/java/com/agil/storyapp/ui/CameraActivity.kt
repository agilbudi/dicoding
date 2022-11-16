package com.agil.storyapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.agil.storyapp.R
import com.agil.storyapp.databinding.ActivityCameraBinding
import com.agil.storyapp.utils.UserPreference
import com.agil.storyapp.utils.createFile

class CameraActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            updateState(savedInstanceState)
        }

        binding.ivCameraCapture.setOnClickListener(this)
        binding.ivCameraSwitch.setOnClickListener(this)
    }

    private fun updateState(savedInstanceState: Bundle) {
        val isBackCamera = savedInstanceState.getBoolean("isBackCamera")
        cameraSelector =
            if (isBackCamera) CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA
        startCamera()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(outputOption, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra("picture", photoFile)
                    intent.putExtra(
                        "isBackCamera",
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    setResult(AddStoryActivity.CAMERA_X_RESULT, intent)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        getString(R.string.Failed_to_take_a_picture),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector,
                    preview, imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(this, getString(R.string.Failed_to_open_camera), Toast.LENGTH_SHORT)
                    .show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                permissionsNotGranted()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun permissionsNotGranted() {
        val userPreference = UserPreference(this)
        val alertDialog = AlertDialog.Builder(this).setTitle("Permissions required")
        with(alertDialog) {
            val remaining = 2
            setMessage(
                "These permissions are required to use this Camera. \nPlease allow Camera permissions first," +
                        " ${remaining - userPreference.getGrantCamera()} attempt remaining."
            )
            if (userPreference.getGrantCamera() < 2) {
                setNegativeButton("don't allow") { _, _ -> finish() }
                setPositiveButton("Grant") { _, _ ->
                    userPreference.setGrantCamera()
                    onRequestPermissions()
                }
            } else {
                setPositiveButton("can not permissions") { _, _ ->
                    Toast.makeText(
                        this@CameraActivity,
                        "Please go to setting to change camera permission!",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
            show()
        }
    }

    private fun onRequestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.ivCameraCapture.id -> takePhoto()
            binding.ivCameraSwitch.id -> {
                cameraSelector =
                    if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
                startCamera()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!allPermissionsGranted()) {
            permissionsNotGranted()
        } else {
            startCamera()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isBackCamera", cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}