package com.hide09.androidfundamental.broadcastevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hide09.androidfundamental.R
import com.hide09.androidfundamental.databinding.ActivitySmsReceiverBinding

class SmsReceiverActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_SMS_NO = "extra_sms_no"
        const val EXTRA_SMS_MESSAGE = "extra_sms_massage"
    }
    private var binding: ActivitySmsReceiverBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsReceiverBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        title = getString(R.string.incoming_message)
        val senderNo = intent.getStringExtra(EXTRA_SMS_NO)
        val senderMessage = intent.getStringExtra(EXTRA_SMS_MESSAGE)

        binding?.btnClose?.setOnClickListener {
            finish()
        }
        binding?.tvFrom?.text = getString(R.string.from, senderNo)
        binding?.tvMessage?.text = senderMessage
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}