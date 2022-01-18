package com.example.timer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class StartedTimer : AppCompatActivity() {

    private var repeatSeconds: Int = 0
    private var repeats: Int = 0
    private var currentrepeats: Int = 0
    private var LOG_TAG: String? = ""
    private var currentSeconds = 0
    private var togo_in_percent: Float = 100f
    private val timer = Timer()
    private lateinit var pb: ProgressBar
    private lateinit var timerText: TextView
    private lateinit var repeatsText: TextView

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

        progress()
    }

    private fun progress() {
        currentSeconds = repeatSeconds
        Log.d(LOG_TAG, "new timer")

        timer.schedule(object : TimerTask() {
            override fun run() {
                togo_in_percent = (currentSeconds.toFloat() / repeatSeconds.toFloat()) * 100
                Log.d(LOG_TAG, "${togo_in_percent.toInt()}% = $currentSeconds / $repeatSeconds")

                pb.progress = togo_in_percent.toInt()

                setTimerText(currentSeconds)

                if (currentSeconds == 0) {
                    Log.d(LOG_TAG, "timer finished")

                    if (currentrepeats <= repeats) {
                        currentrepeats++
                        //repeatsText.text = "repeats: ${repeats - currentrepeats}" somehow issues
                        progress()
                    } else {
                        pb.progress = 0
                    }
                    timer.cancel()
                }
                currentSeconds--
            }
        }, 0, 1000)
    }

    fun stopTimer(view: View){
        timer.cancel()
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