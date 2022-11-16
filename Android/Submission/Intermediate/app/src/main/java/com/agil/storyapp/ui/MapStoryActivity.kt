package com.agil.storyapp.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.agil.storyapp.MainActivity
import com.agil.storyapp.R
import com.agil.storyapp.adapter.MapPagingAdapter
import com.agil.storyapp.data.local.entity.StoryEntity
import com.agil.storyapp.data.repository.Result
import com.agil.storyapp.databinding.ActivityMapStoryBinding
import com.agil.storyapp.model.User
import com.agil.storyapp.ui.viewmodel.MapViewModel
import com.agil.storyapp.ui.viewmodel.MapViewModelFactory
import com.agil.storyapp.utils.UserPreference
import com.agil.storyapp.utils.getCurrentLocation
import com.agil.storyapp.utils.isLocationEnabled
import com.agil.storyapp.utils.requestMapPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class MapStoryActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnPoiClickListener,
    GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapStoryBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var user: User
    private lateinit var factory: MapViewModelFactory
    private val viewModel: MapViewModel by viewModels{ factory }
    private val mapPagingAdapter = MapPagingAdapter()
    private var infoStoryEntity = ArrayList<StoryEntity>()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mSelectedMarker: Marker? = null
    private var lastKnownLocation: Location? = null
    private var tripMarker = ArrayList<Marker>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        factory = MapViewModelFactory.getInstance(this)
        binding = ActivityMapStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        supportActionBar?.title = "Map Story"
        mUserPreference = UserPreference(this)
        user = mUserPreference.getUser()
        if (savedInstanceState != null) {
            updateState(savedInstanceState)
        }

        binding.rvMapStory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mapPagingAdapter
        }
        updateListStory(user)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.srMapStory.setOnRefreshListener { updateListStory(user) }
        binding.fabAddMapStory.setOnClickListener { addStory() }
        mapPagingAdapter.setOnItemClickCallback(object : MapPagingAdapter.OnItemClickCallback{
            override fun onItemClicked(data: StoryEntity) {
                selectedItem(data)
            }
            override fun onDetailItemClicked(data: StoryEntity) {
                showDetail(data)
            }
        })
    }

    private fun showDetail(data: StoryEntity) {
        val intent = Intent(this, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.EXTRA_STORY, data)
        startActivity(intent)
    }

    private fun addStory() {
        startActivity(Intent(this, AddStoryActivity::class.java))
    }

    @Suppress("DEPRECATION")
    private fun updateState(savedInstanceState: Bundle) {
        val firstActivity = mUserPreference.firstActivity()
        lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
        if (user.token.isEmpty()) {
            startActivity(Intent(this, AuthenticationActivity::class.java))
            finish()
        }else{
            if (firstActivity != TAG){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            updateListStory(user)
            val camera = lastKnownLocation
            if (camera != null) {
                moveZoomingCamera(LatLng(camera.latitude,camera.longitude), 15F)
            }else{
                moveZoomingCamera(DEFAULT_MARKER_POSITION, 12F)
            }
        }
    }

    private fun updateListStory(user: User) {
        viewModel.listStory(user.token).observe(this){ result ->
            when(result){
                is Result.Success ->{ mapPagingAdapter.submitData(lifecycle, result.data) }
                is Result.Error ->{
                    Toast.makeText(this, result.error,Toast.LENGTH_SHORT).show()
                    binding.srMapStory.isRefreshing = false
                }
                is Result.Loading -> binding.srMapStory.isRefreshing = true
            }
        }
    }

    private fun selectedItem(data: StoryEntity) {
        val lat = data.lat?.toDouble()
        val lng = data.lon?.toDouble()
        if (lat != null && lng != null) {
            moveZoomingCamera(LatLng(lat, lng), 12F)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val lat = if (user.lat?.toDouble() != 0.0) user.lat else null
        val lon = if (user.lon?.toDouble() != 0.0) user.lon else null
        val position = if (lat != null && lon != null) LatLng(lat.toDouble(),lon.toDouble()) else DEFAULT_MARKER_POSITION

        uiSetting()
        setMapStyle()
        setUpPermission()
        showMarker()
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnPoiClickListener(this)
        mMap.setOnMapClickListener(this)
        mMap.setOnMapLongClickListener(this)

        moveZoomingCamera(position, 12F)
    }


    private fun setUpPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        }else{
            requestMapPermission(this)
        }
    }

    private fun uiSetting() {
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled  = true
    }


    private fun getAvailableGPS(): Boolean {
        val alertDialog = AlertDialog.Builder(this).setTitle("GPS Location required")
        with(alertDialog){
            setMessage("These are required to use GPS Location, " +
                    "\nPlease 'Turn On' to get your location.")
            setPositiveButton("Open Setting"){_,_ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            setNegativeButton("Cancel"){ dialogInterface,_ ->
                dialogInterface.cancel()
            }
        }

            if (isLocationEnabled(this)){
                if (ActivityCompat.checkSelfPermission(
                        this.applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this.applicationContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestMapPermission(this)
                }
                return true
            }else{
                alertDialog.show()
            }

        return false
    }

    private fun showMarker(){
        viewModel.markerStory().observe(this){ result ->
            when(result){
                is Result.Loading -> showLoading(true)
                is Result.Error -> {
                    showToast(this,result.error, true)
                    showLoading(false)
                }
                is Result.Success -> {
                    result.data.forEach {
                        val story = ArrayList<StoryEntity>()
                        try {
                            val lat = it.lat
                            val lng = it.lon
                            val snippet = if (it.id == user.userId){
                                resources.getString(R.string.your_story)
                            }else it.description
                            val img = it.photoUrl.ifEmpty {
                                ContextCompat.getDrawable(this, R.mipmap.ic_image_default)
                                    .toString()
                            }

                            if (lat != null && lng != null) {
                                val latLng = LatLng(lat.toDouble(),lng.toDouble())
                                mMap.addMarker(MarkerOptions().position(latLng).title(it.name).snippet(snippet)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                )
                            }
                            story.add(StoryEntity(it.id, it.name, it.description, img, it.createdAt, lat, lng))
                        }catch (e: NullPointerException){
                            Log.e(TAG, e.message.toString())
                        }
                        infoStoryEntity.addAll(story)
                        showLoading(false)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_map_story, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_list_story ->{
                mUserPreference.setMainActivity(MainActivity.TAG)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                true
            }
            R.id.menu_logout -> {
                val removeState = mUserPreference.delUser()
                if (removeState) {
                    startActivity(Intent(this, AuthenticationActivity::class.java))
                    finish()
                    true
                } else {
                    showToast(this, "Logout failed", true)
                    true
                }
            }
            R.id.menu_exit -> {
                finish()
                true
            }
            else -> true
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        if(getAvailableGPS()) {
            try {
                val location = run{  getCurrentLocation(this@MapStoryActivity) }
                if (location != null) {
                    lastKnownLocation = location
                    mUserPreference.setLocation(location)
                }
            }catch (e: SecurityException){
                Log.e("Exception: %s", e.message, e)
            }
        }
        return false
    }
    override fun onPoiClick(poi: PointOfInterest) {
        resetSelectedMarker()
        val poiMarker = mMap.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
        if (poiMarker != null) {
            mSelectedMarker = poiMarker
            poiMarker.showInfoWindow()
        }
    }
    override fun onMapClick(latLng: LatLng) {
        removeTripMarker()
        resetSelectedMarker()
    }
    override fun onMapLongClick(latLng: LatLng) {
        resetSelectedMarker()
        val marker = mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("New marker")
                .snippet("lat:${latLng.latitude}, lng:${latLng.longitude}")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )
        if (marker != null) {
            tripMarker.add(marker)
            marker.showInfoWindow()
        }
    }

    private fun removeTripMarker(){
        for(marker in tripMarker){
            marker.remove()
        }
    }
    private fun resetSelectedMarker(){
            mSelectedMarker?.isVisible = false
            mSelectedMarker = null
            removeTripMarker()
    }
    private fun moveZoomingCamera(latLng: LatLng, zoom: Float) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }
    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success){
                Log.e(TAG, "Style parsing failed")
            }
        }catch (e: Resources.NotFoundException){
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext,"Location Permission Granted",Toast.LENGTH_SHORT).show()
                setUpPermission()
            }else {
                Toast.makeText(applicationContext, "Location Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun showLoading(isLoading: Boolean){
        binding.mapProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(context: Context, text:String, isLong:Boolean){
        if(isLong){
            Toast.makeText(context,text,Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context,text,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        super.onSaveInstanceState(outState, outPersistentState)
    }
    companion object{
        private val DEFAULT_MARKER_POSITION = LatLng(-7.797068, 110.370529)
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
        private const val KEY_LOCATION = "location"
        val TAG: String = MapStoryActivity::class.java.simpleName
    }

}
