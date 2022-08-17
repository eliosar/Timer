package com.example.timer

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmTimer : AppCompatActivity() {
    private lateinit var LOG_TAG: String
    private lateinit var currentSound: MediaPlayer

    @SuppressLint("SetTextI18n", "InvalidWakeLockTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmtimer)

        LOG_TAG = MainActivity::LOG_TAG.get(MainActivity())

        findViewById<TextView>(R.id.repeats).text = "repeats: ${intent.getIntExtra(StartedTimer::currentRepeatsLeftLocation.get(StartedTimer()), 0)}"
        Log.d(LOG_TAG, "confirm timer")

        currentSound = MediaPlayer()
        currentSound.setDataSource(getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE).getString("currentSoundPath", null))
        currentSound.prepare()

        currentSound.start()
    }

    fun stopTimer(view: View){
        currentSound.stop()
        StartedTimer.cancelTimer()

        finish()
    }

    fun confirmTimer(view: View){
        currentSound.stop()
        StartedTimer.newtimer = true
        StartedTimer.nextWindow = false
        Log.d(LOG_TAG, "timer confirmed")

        finish()
    }
}