package com.agil.myandroidintermediate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.agil.myandroidintermediate.advanced_ui.CustomViewActivity
import com.agil.myandroidintermediate.advanced_ui.tiketing.TiketingActivity
import com.agil.myandroidintermediate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnCustomView.setOnClickListener(this@MainActivity)
            btnTiketing.setOnClickListener(this@MainActivity)
            btnCanvas.setOnClickListener(this@MainActivity)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.btnCustomView.id -> startActivity(Intent(this, CustomViewActivity::class.java))
            binding.btnTiketing.id -> startActivity(Intent(this, TiketingActivity::class.java))
        }
    }
}