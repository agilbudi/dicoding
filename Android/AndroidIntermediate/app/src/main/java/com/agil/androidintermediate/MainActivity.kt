package com.agil.androidintermediate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.agil.androidintermediate.advanced_ui.canvas.CanvasActivity
import com.agil.androidintermediate.advanced_ui.custom_view.CustomViewActivity
import com.agil.androidintermediate.advanced_ui.ticketing.TicketingActivity
import com.agil.androidintermediate.advanced_ui.webview.WebViewActivity
import com.agil.androidintermediate.advanced_ui.widget.WidgetActivity
import com.agil.androidintermediate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            btnCustomView.setOnClickListener(this@MainActivity)
            btnTicketing.setOnClickListener(this@MainActivity)
            btnCanvas.setOnClickListener(this@MainActivity)
            btnWidget.setOnClickListener(this@MainActivity)
            btnWebview.setOnClickListener(this@MainActivity)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            binding.btnCustomView.id -> startActivity(Intent(this, CustomViewActivity::class.java))
            binding.btnTicketing.id -> startActivity(Intent(this, TicketingActivity::class.java))
            binding.btnCanvas.id -> startActivity(Intent(this, CanvasActivity::class.java))
            binding.btnWidget.id -> startActivity(Intent(this, WidgetActivity::class.java))
            binding.btnWebview.id -> startActivity(Intent(this, WebViewActivity::class.java))
        }
    }
}