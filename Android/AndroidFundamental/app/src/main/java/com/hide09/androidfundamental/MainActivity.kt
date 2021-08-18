package com.hide09.androidfundamental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hide09.androidfundamental.activityevent.MainEvenActivity
import com.hide09.androidfundamental.alarmevent.MainAlarmActivity
import com.hide09.androidfundamental.broadcastevent.MainBroadcastActivity
import com.hide09.androidfundamental.databinding.ActivityMainBinding
import com.hide09.androidfundamental.fragmentevent.MainFragmentActivity
import com.hide09.androidfundamental.listviewevent.MainListviewActivity
import com.hide09.androidfundamental.navigationevent.MainNavigationActivity
import com.hide09.androidfundamental.serviceevent.MainServiceActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMainActivity.setOnClickListener(this)
        binding.btnMainFragment.setOnClickListener(this)
        binding.btnMainListview.setOnClickListener(this)
        binding.btnMainNav.setOnClickListener(this)
        binding.btnMainService.setOnClickListener(this)
        binding.btnMainBroadcast.setOnClickListener(this)
        binding.btnMainAlarm.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_main_activity->{
                startActivity(Intent(this, MainEvenActivity::class.java))
            }
            R.id.btn_main_fragment ->{
                startActivity(Intent(this, MainFragmentActivity::class.java))
            }
            R.id.btn_main_listview ->{
                startActivity(Intent(this, MainListviewActivity::class.java))
            }
            R.id.btn_main_nav ->{
                startActivity(Intent(this, MainNavigationActivity::class.java))
            }
            R.id.btn_main_service ->{
                startActivity(Intent(this, MainServiceActivity::class.java))
            }
            R.id.btn_main_broadcast ->{
                startActivity(Intent(this, MainBroadcastActivity::class.java))
            }
            R.id.btn_main_alarm ->{
                startActivity(Intent(this, MainAlarmActivity::class.java))
            }
        }
    }
}