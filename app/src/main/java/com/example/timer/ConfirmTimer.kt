package com.example.timer

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.NotificationCompat

class ConfirmTimer : AppCompatActivity() {
    private var LOG_TAG = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmtimer)

        var currentrepeatsleft = 0

        LOG_TAG = intent.getStringExtra(StartedTimer::logTag.get(StartedTimer())).toString()
        currentrepeatsleft = intent.getIntExtra(StartedTimer::currentrepeatsleftlocation.get(StartedTimer()), currentrepeatsleft)

        findViewById<TextView>(R.id.repeats).text = "repeats: $currentrepeatsleft"
        Log.d(LOG_TAG, "confirm timer")
    }

    fun stopTimer(view: View){
        StartedTimer.stopTimer(view)
        finish()
    }

    fun confirmTimer(view: View){
        Log.d(LOG_TAG, "timer confirmed")
        StartedTimer.newtimer = true
        finish()
    }
}