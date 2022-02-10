package com.example.timer

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.NotificationCompat
import java.util.*

open class StartedTimer : AppCompatActivity() {

    val currentrepeatsleftlocation = "com.example.timer.currentrepeats"
    var logTag = "com.example.logTag"
    private var repeatSeconds: Int = 0
    private var togo_in_percent: Float = 100f
    private lateinit var timerText: TextView
    private lateinit var repeatsText: TextView
    companion object : StartedTimer() {
        var newtimer = true
        private var LOG_TAG = ""
        private var timer: Timer? = Timer()
        private lateinit var pb: ProgressBar
        private var currentrepeats = 1
        private var repeats: Int = 0

        fun stopTimer(view: View){
            Log.d(LOG_TAG, "timer manual finished")
            newtimer = false
            timer?.cancel()
            timer?.purge()
            timer = null
            timer = Timer()
            pb.progress = 0
            currentrepeats = repeats + 1
        }
    }
    @SuppressLint("SetTextI18n")
    private val timerthread = Thread {
        while (currentrepeats <= repeats){
            if(newtimer) {
                newtimer = false
                Log.d(LOG_TAG, "$currentrepeats < $repeats")
                Handler(Looper.getMainLooper()).postDelayed({
                    progress()
                    repeatsText.text = "repeats: ${repeats - currentrepeats}"
                }, 0)
            }
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_started_timer)

        repeatSeconds = intent.getIntExtra(MainActivity::repeatMinutesLoc.get(MainActivity()), repeatSeconds)
        repeatSeconds *= 60
        repeats = intent.getIntExtra(MainActivity::repeats.get(MainActivity()), repeats)
        LOG_TAG = intent.getStringExtra(MainActivity::logTag.get(MainActivity())).toString()
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

                    startconfirmActivity()

                    currentrepeats++
                }
                currentSeconds--
            }
        }, 0, 1000) // if program finished period = 1000
    }

    fun stop_Timer(view: View){
        stopTimer(view)
    }

    fun startconfirmActivity(){
        val intent = Intent(this, ConfirmTimer::class.java)
        intent.putExtra(currentrepeatsleftlocation, repeats - currentrepeats)
        intent.putExtra(logTag, LOG_TAG)

        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun setTimerText(currentSeconds: Int){
        val seconds = ((currentSeconds % 86400) % 3600) % 60
        val minutes = ((currentSeconds % 86400) % 3600) / 60
        val hours = ((currentSeconds % 86400) / 3600)

        timerText.text =  "${String.format("%02d", hours)} : ${String.format("%02d", minutes)} : ${String.format("%02d", seconds)}"
    }
}