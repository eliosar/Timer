package com.example.timer

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.NotificationCompat

class ConfirmTimer : AppCompatActivity() {
    private var LOG_TAG = ""
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmtimer)

        var currentrepeatsleft = 0

        LOG_TAG = intent.getStringExtra(StartedTimer::logTag.get(StartedTimer())).toString()
        currentrepeatsleft = intent.getIntExtra(StartedTimer::currentrepeatsleftlocation.get(StartedTimer()), currentrepeatsleft)

        findViewById<TextView>(R.id.repeats).text = "repeats: $currentrepeatsleft"
        Log.d(LOG_TAG, "confirm timer")

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "TimerReminderChannel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("lol", name, importance)
            val notifymanager = getSystemService(NotificationManager::class.java)
            notifymanager.createNotificationChannel(channel)
        }

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent)
    }

    fun stopTimer(view: View){
        StartedTimer.stopTimer(view)
        stopNotification()
        finish()
    }

    fun confirmTimer(view: View){
        Log.d(LOG_TAG, "timer confirmed")
        StartedTimer.newtimer = true
        stopNotification()
        finish()
    }

    private fun stopNotification(){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}