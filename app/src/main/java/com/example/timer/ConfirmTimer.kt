package com.example.timer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmTimer : AppCompatActivity() {
    private lateinit var LOG_TAG: String
    private var repeatsLeft = 0
    private val currentSound = MediaPlayer()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmtimer)

        LOG_TAG = MainActivity.LOG_TAG
        repeatsLeft = intent.getIntExtra(StartedTimer::repeatsLeftLocation.get(StartedTimer()), 0)

        findViewById<TextView>(R.id.repeats).text = "repeats: $repeatsLeft"
        Log.d(LOG_TAG, "confirm timer")

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
        Log.d(LOG_TAG, "timer confirmed")

        currentSound.stop()
        StartedTimer.newTimer = true

        if(repeatsLeft == 0){
            startActivity(Intent(this, MainActivity::class.java))
        }

        finish()
    }
}