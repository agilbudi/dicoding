package com.hide09.consumergithub.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.hide09.consumergithub.MainActivity
import com.hide09.consumergithub.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver: BroadcastReceiver(){
    companion object{
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TITLE = "title"

        private const val ID_REPEATING = 101
        private const val TIME_FORMAT = "HH:mm"
    }
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        showToast(context, "$title: $message", true)
        if (message != null){
            showAlarmNotification(context, title, message, ID_REPEATING)
        }
    }

    fun setRepeatingAlarm(context: Context, title: String, time: String, message: String){
        if(isDateInvalid(time, TIME_FORMAT)) return showToast(context, "Date Invalid", false)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TITLE, title)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val hour = Integer.parseInt(timeArray[0])
        val minute: String = timeArray[1]

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        when {
            hour > 12 -> {
                var newHour = hour
                newHour -= 12
                showToast(context, "Repeat alarm set up $newHour:$minute PM",false)
            }
            hour == 0 -> {
                showToast(context, "Repeat alarm set up 12:$minute AM", false)
            }
            else -> {
                showToast(context, "Repeat alarm set up $hour:$minute AM", false)
            }
        }
    }

    private fun showAlarmNotification(context: Context, title: String?, message: String, notifId: Int){
        val myVibrate = longArrayOf(1000, 1000, 1000, 1000, 1000)
        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context)
                .addParentStack(MainActivity::class.java)
                .addNextIntent(intent)
                .getPendingIntent(notifId, PendingIntent.FLAG_UPDATE_CURRENT)
        val notifManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setContentIntent(pendingIntent)
                .setVibrate(myVibrate)
                .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = myVibrate
            builder.setChannelId(channelId)
            notifManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notifManagerCompat.notify(notifId, notification)
    }
    fun cancelAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }

    private fun showToast(context: Context, message: String?, durationLong: Boolean) {
        if (durationLong) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isDateInvalid(date: String, format: String): Boolean{
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        }catch (e: ParseException){
            true
        }
    }
}