package com.hide09.androidfundamental.alarmevent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hide09.androidfundamental.R
import com.hide09.androidfundamental.databinding.ActivityMainAlarmBinding
import java.text.SimpleDateFormat
import java.util.*

class MainAlarmActivity : AppCompatActivity(), View.OnClickListener, DatePickerFragment.DialogDateListener, TimePickerFragment.DialogTimeListener {
    private var binding: ActivityMainAlarmBinding? = null
    private lateinit var alarmReceiver: AlarmReceiver

    companion object{
        private const val DATE_PICKER_TAG = "DatePicker"
        private const val TIME_PICKER_ONCE_TAG = "TimePickerOnce"
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAlarmBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.btnOnceDate?.setOnClickListener(this)
        binding?.btnOnceTime?.setOnClickListener(this)
        binding?.btnSetOnceAlarm?.setOnClickListener(this)
        binding?.btnRepeatingTime?.setOnClickListener(this)
        binding?.btnSetRepeatingAlarm?.setOnClickListener(this)
        binding?.btnCancelRepeatingAlarm?.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_once_date ->{
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, DATE_PICKER_TAG)
            }
            R.id.btn_once_time ->{
                val timePickerFragment = TimePickerFragment()
                timePickerFragment.show(supportFragmentManager, TIME_PICKER_ONCE_TAG)
            }
            R.id.btn_set_once_alarm ->{
                val onceDate = binding?.tvOnceDate?.text.toString()
                val onceTime = binding?.tvOnceTime?.text.toString()
                val onceMessage = binding?.edtOnceMessage?.text.toString()

                alarmReceiver.setOneTimeAlarm(this, AlarmReceiver.TYPE_ONE_TIME, onceDate, onceTime, onceMessage)
            }
            R.id.btn_repeating_time->{
                val timePickerFragmentRepeat = TimePickerFragment()
                timePickerFragmentRepeat.show(supportFragmentManager, TIME_PICKER_REPEAT_TAG)
            }
            R.id.btn_set_repeating_alarm->{
                val repeatTime = binding?.tvRepeatingTime?.text.toString()
                val repeatMessage = binding?.edtRepeatingMessage?.text.toString()
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.TYPE_REPEATING, repeatTime, repeatMessage)
            }
            R.id.btn_cancel_repeating_alarm-> alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
        }
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        binding?.tvOnceDate?.text = dateFormat.format(calendar.time)
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when(tag){
            TIME_PICKER_ONCE_TAG -> binding?.tvOnceTime?.text = dateFormat.format(calendar.time)
            TIME_PICKER_REPEAT_TAG -> binding?.tvRepeatingTime?.text = dateFormat.format(calendar.time)
            else ->{

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}