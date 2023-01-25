package com.example.timer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


open class StartedTimer : AppCompatActivity() {
    val repeatsLeftLocation = "com.example.timer.repeatsLeft"
    private var repeatSeconds = 0
    private lateinit var repeatsText: TextView
    companion object {
        private lateinit var startedTimer: StartedTimer
        private var minusSeconds = 1
        private lateinit var currentSound: MediaPlayer
        private val LOG_TAG = MainActivity.LOG_TAG
        private var timer = Timer()
        private var repeatsLeft = 1
        private var repeats: Int = 0
        var newTimer = true

        fun cancelTimer(){
            Log.d(LOG_TAG, "timer manual finished")

            resetTimer()

            startedTimer.startActivity(Intent(startedTimer, MainActivity::class.java))
            startedTimer.finish()
        }

        private fun resetTimer(){
            timer.cancel()
            timer.purge()
            timer = Timer()
        }
    }

    @SuppressLint("SetTextI18n")
    private val timerthread = Thread {
        repeatsLeft = repeats - 1
        newTimer = true
        do{
            if(newTimer) {
                newTimer = false
                Log.d(LOG_TAG, "${repeats - repeatsLeft}. Timer")
                Handler(Looper.getMainLooper()).postDelayed({
                    progress()
                    repeatsText.text = "repeats: $repeatsLeft"
                }, 0)
            }
        }while (repeatsLeft >= 0)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_started_timer)

        repeatSeconds = intent.getIntExtra(MainActivity::repeatMinutesLoc.get(MainActivity()), 0) * 60
        repeats = intent.getIntExtra(MainActivity::repeats.get(MainActivity()), 0)

        repeatsText = findViewById(R.id.repeatsText)
        repeatsText.text = "repeats: $repeats"

        startedTimer = this

        currentSound = MediaPlayer().also {
            it.setDataSource(getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE).getString("currentSoundPath", null))
        }

        timerthread.start()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean){
        if(currentSound.isPlaying && hasFocus){
            startConfirmTimerActivity()
        }
    }

    private fun progress() {
        var currentSeconds = repeatSeconds
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val togoInPercent = ((currentSeconds.toFloat() / repeatSeconds.toFloat()) * 100).toInt()
                findViewById<ProgressBar>(R.id.timeprogressline).progress = togoInPercent

                setTimerText(currentSeconds)

                if (currentSeconds == 0) {
                    Log.d(LOG_TAG, "timer finished")

                    resetTimer()
                    startConfirmActivity()

                    repeatsLeft--
                }
                currentSeconds -= minusSeconds
            }
        }, 0, 100) // if program finished period = 1000
    }

    fun cancelTimer(view: View){
        cancelTimer()
    }

    @SuppressLint("SetTextI18n")
    fun stopResumeTimer(view: View){
        view as Button

        if(view.text == "stop Timer") {
            Log.d(LOG_TAG, "timer stoped")
            minusSeconds = 0
            view.text = "resume Timer"
        }else{
            Log.d(LOG_TAG, "timer resume")
            minusSeconds = 1
            view.text = "stop Timer"
        }
    }

    fun startConfirmActivity(){
        currentSound.prepare()

        currentSound.start()

        Log.d(LOG_TAG, "has focus: ${hasWindowFocus()}")
        if(hasWindowFocus()){
            startConfirmTimerActivity()
        }
    }

    private fun startConfirmTimerActivity(){
        currentSound.stop()

        startActivity(Intent(this, ConfirmTimer::class.java).also {
            it.putExtra(repeatsLeftLocation, repeatsLeft)
        })

        if(repeatsLeft <= 0){
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTimerText(currentSeconds: Int){
        val seconds = ((currentSeconds % 86400) % 3600) % 60
        val minutes = ((currentSeconds % 86400) % 3600) / 60
        val hours = ((currentSeconds % 86400) / 3600)

        findViewById<TextView>(R.id.timerText).text =  "${String.format("%02d", hours)} : ${String.format("%02d", minutes)} : ${String.format("%02d", seconds)}"
    }
}