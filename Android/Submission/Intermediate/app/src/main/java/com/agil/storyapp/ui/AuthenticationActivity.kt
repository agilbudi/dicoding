package com.agil.storyapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.agil.storyapp.R
import com.agil.storyapp.databinding.ActivityAuthenticationBinding
import com.agil.storyapp.ui.auth.RegisterFragment

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState != null) {
            updateState()
        }
    }

    private fun updateState() {
        if (RegisterFragment().isInLayout) {
            replaceFragment(RegisterFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val mFragmentManager = supportFragmentManager
        val fragmentTransaction = mFragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fg_auth, fragment)
            .commit()
    }
}