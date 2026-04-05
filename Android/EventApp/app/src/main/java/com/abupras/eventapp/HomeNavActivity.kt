package com.abupras.eventapp

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.abupras.eventapp.databinding.ActivityHomeNavBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeNavActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)

        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home_nav) as NavHostFragment
        val navController = navHostFragment.navController
        setSupportActionBar(binding.toolbarHome)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val pref = SettingPreferences.getInstance(application.dataStore)
        ViewModelProvider(this, ViewModelFactory(pref))[HomeNavViewModel::class]

        navView.setupWithNavController(navController)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.theme_menu, menu)

        val switchItem = menu.findItem(R.id.switch_theme)
        val actionView = switchItem.actionView
        val switchTheme = actionView?.findViewById<SwitchCompat>(R.id.swv_theme)
        val tvSwitchTitle = actionView?.findViewById<TextView>(R.id.tv_switch_title)
        val pref = SettingPreferences.getInstance(application.dataStore)
        val viewModelSetting =
            ViewModelProvider(this, ViewModelFactory(pref))[HomeNavViewModel::class]

        viewModelSetting.getTitleSettings().observe(this) { title ->
            tvSwitchTitle?.text = title
        }
        switchTheme?.setOnCheckedChangeListener { _, isChecked ->
            viewModelSetting.saveThemeSetting(isChecked)
            if (!isChecked) {
                viewModelSetting.saveTitleSetting("Day Theme")
            } else {
                viewModelSetting.saveTitleSetting("Night Theme")
            }
        }
        viewModelSetting.getThemeSettings().observe(this) { isNightMode: Boolean ->
            if (isNightMode) {
                switchTheme?.isChecked = true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                switchTheme?.isChecked = false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        return true
    }

    fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    fun showLoading(isLoading: Boolean) {
        binding.pgBarHome.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}