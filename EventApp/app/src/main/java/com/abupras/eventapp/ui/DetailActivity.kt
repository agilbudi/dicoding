package com.abupras.eventapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abupras.eventapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra(EVENTS, 0)
        Toast.makeText(this, "dengan id: $id", Toast.LENGTH_SHORT).show()
    }
    companion object{
        private const val EVENTS = "EVENTS"
    }
}