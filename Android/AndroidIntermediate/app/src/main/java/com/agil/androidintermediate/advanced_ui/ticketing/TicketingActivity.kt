package com.agil.androidintermediate.advanced_ui.ticketing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.agil.androidintermediate.databinding.ActivityTiketingBinding

class TicketingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTiketingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTiketingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.finishButton.setOnClickListener {
            binding.seatsView.seat?.let {
                Toast.makeText(this@TicketingActivity, "Kursi anda Nomor ${it.name}",
                    Toast.LENGTH_SHORT).show()
            }?: run{
                Toast.makeText(this@TicketingActivity,
                    "Silakan pilih kursi terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}