package com.agil.storyapp.utils

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.agil.storyapp.R
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"
private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun String.withDateFormat(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val date = format.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(date)
}
fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory =
        if (mediaDir != null && mediaDir.exists()) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    return if (isBackCamera) {
        matrix.postRotate(90f)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        matrix.postRotate(-90f)
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun getCurrentLocation(activity: Activity): Location? {
    var currentLocation: Location? = null
    var locationByGps : Location? = null
    var locationByNetwork: Location? = null

    val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    val gpsLocationListener = LocationListener { location -> locationByGps= location }
    val networkLocationListener = LocationListener { location -> locationByNetwork= location }

    if (ActivityCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            activity.applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        requestMapPermission(activity)
    }

    if (hasGps) {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            0F,
            gpsLocationListener
        )
    }
    if (hasNetwork) {
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            5000,
            0F,
            networkLocationListener
        )
    }
    val lastKnownLocationByGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    lastKnownLocationByGps?.let { locationByGps = lastKnownLocationByGps }
    val lastKnownLocationByNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    lastKnownLocationByNetwork?.let { locationByNetwork = lastKnownLocationByNetwork }
    if (locationByGps != null && locationByNetwork != null) {
        currentLocation = if (locationByGps!!.accuracy > locationByNetwork!!.accuracy) {
            locationByGps
        } else {
            locationByNetwork
        }
    }
    return currentLocation
}


fun requestMapPermission(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity, arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION),
        PERMISSION_REQUEST_ACCESS_LOCATION
    )
}

fun checkMapPermissions(context: Context): Boolean {
    if (ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED
    ){
        return true
    }
    return false
}

fun isLocationEnabled(activity: Activity): Boolean {
    val mLocation = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return mLocation
        .isProviderEnabled(LocationManager.GPS_PROVIDER)
            || mLocation
        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}