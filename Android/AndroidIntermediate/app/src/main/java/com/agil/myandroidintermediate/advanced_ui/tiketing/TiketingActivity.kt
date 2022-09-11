package com.agil.myandroidintermediate.advanced_ui.tiketing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.agil.myandroidintermediate.databinding.ActivityTiketingBinding

class TiketingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTiketingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTiketingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        with(binding){
            finishButton.setOnClickListener {
                seatsView.seat?.let {
                    Toast.makeText(this@TiketingActivity, "Kursi anda Nomor ${it.name}",
                        Toast.LENGTH_SHORT).show()
                }?: run{
                    Toast.makeText(this@TiketingActivity,
                        "Silakan pilih kursi terlebih dahulu.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}