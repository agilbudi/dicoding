package com.abupras.eventapp

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.abupras.eventapp.databinding.ActivityHomeNavBinding

class HomeNavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home_nav) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }

    fun setTitle(title: String){
        binding.tvHomeTitle.text = title
    }
    fun showLoading(isLoading: Boolean){
            binding.pgBarHome.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}