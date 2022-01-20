package com.example.timer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import java.util.*
import java.util.concurrent.Delayed

class StartedTimer : AppCompatActivity() {

    private var repeatSeconds: Int = 0
    private var repeats: Int = 0
    private var LOG_TAG: String? = ""
    private var newtimer = true
    private var togo_in_percent: Float = 100f
    private var currentrepeats = 1
    private var timer: Timer? = Timer()
    private lateinit var pb: ProgressBar
    private lateinit var timerText: TextView
    private lateinit var repeatsText: TextView
    private val timerthread = Thread {
        while (currentrepeats <= repeats){
            if(newtimer) {
                Log.d(LOG_TAG, "$currentrepeats < $repeats")
                newtimer = false
                Handler(Looper.getMainLooper()).postDelayed({
                    progress()
                    repeatsText.text = "repeats: ${repeats - currentrepeats}"
                }, 0)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_started_timer)

        repeatSeconds = intent.getIntExtra(MainActivity::repeatMinutes.get(MainActivity()), repeatSeconds)
        repeatSeconds *= 60
        repeats = intent.getIntExtra(MainActivity::repeats.get(MainActivity()), repeats)
        LOG_TAG = intent.getStringExtra(MainActivity::logTag.get(MainActivity()))
        pb = findViewById(R.id.timeprogressline)
        timerText = findViewById(R.id.timerText)
        repeatsText = findViewById(R.id.repeatsText)

        repeatsText.text = "repeats: $repeats"
        timerthread.start()
    }

    private fun progress() {
        Log.d(LOG_TAG, "new timer")
        var currentSeconds = repeatSeconds
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                togo_in_percent = (currentSeconds.toFloat() / repeatSeconds.toFloat()) * 100

                pb.progress = togo_in_percent.toInt()

                setTimerText(currentSeconds)

                if (currentSeconds == 0) {
                    Log.d(LOG_TAG, "timer finished")
                    timer?.cancel()
                    timer?.purge()
                    timer = null
                    timer = Timer()

                    currentrepeats++
                    newtimer = true
                }
                currentSeconds--
            }
        }, 0, 1000) // if program finished period = 1000
    }

    fun stopTimer(view: View){
        timer?.cancel()
        timer?.purge()
        timer = null
        timer = Timer()
        pb.progress = 0
        Log.d(LOG_TAG, "timer manual finished")
    }

    @SuppressLint("SetTextI18n")
    private fun setTimerText(currentSeconds: Int){
        val seconds = ((currentSeconds % 86400) % 3600) % 60
        val minutes = ((currentSeconds % 86400) % 3600) / 60
        val hours = ((currentSeconds % 86400) / 3600)

        timerText.text =  "${String.format("%02d", hours)} : ${String.format("%02d", minutes)} : ${String.format("%02d", seconds)}"
    }
}