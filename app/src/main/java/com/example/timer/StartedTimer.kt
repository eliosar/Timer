package com.example.timer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.os.PowerManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.widget.Button
import java.util.concurrent.TimeUnit


open class StartedTimer : AppCompatActivity() {

    val currentRepeatsLeftLocation = "com.example.timer.currentRepeatsLeft"
    private var minusSeconds = 1
    private var currentSeconds = 0
    private var repeatSeconds = 0
    private var alarmManager: AlarmManager? = null
    private var togo_in_percent: Float = 100f
    private lateinit var timerText: TextView
    private lateinit var repeatsText: TextView
    companion object {
        var nextWindow = false
        var newtimer = true
        private var LOG_TAG = ""
        private var timer: Timer? = Timer()
        @SuppressLint("StaticFieldLeak")
        private lateinit var pb: ProgressBar
        private var currentrepeats = 1
        private var repeats: Int = 0

        fun cancelTimer(){
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
                Log.d(LOG_TAG, "$currentrepeats. Timer")
                Handler(Looper.getMainLooper()).postDelayed({
                    progress()
                    repeatsText.text = "repeats: ${repeats - currentrepeats}"
                }, 0)
            }
        }

        finish()
        startActivity(Intent(this, MainActivity::class.java))
        currentrepeats = 1
        newtimer = true
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_started_timer)

        repeatSeconds = intent.getIntExtra(MainActivity::repeatMinutesLoc.get(MainActivity()), 0) * 60
        repeats = intent.getIntExtra(MainActivity::repeats.get(MainActivity()), 0)
        LOG_TAG = MainActivity::LOG_TAG.get(MainActivity())

        pb = findViewById(R.id.timeprogressline)
        timerText = findViewById(R.id.timerText)
        repeatsText = findViewById(R.id.repeatsText)

        repeatsText.text = "repeats: $repeats"
        timerthread.start()
    }

    private fun isScreenAwake(): Boolean{
        return (getSystemService(POWER_SERVICE) as PowerManager).isInteractive
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Log.d(LOG_TAG, "${alarmManager == null}")
        Log.d(LOG_TAG, "${!hasFocus}")

        if(alarmManager == null && !hasFocus && isScreenAwake()){
            if(!nextWindow) {
                Log.d(LOG_TAG, "new scheduled alarm")
                scheduleNotification()
            }
        }

        if(alarmManager != null && hasFocus){
            Log.d(LOG_TAG, "delete of scheduled alarm")
            alarmManager = null
        }
    }

    private fun progress() {
        currentSeconds = repeatSeconds
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

                    startConfirmActivity()

                    currentrepeats++
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

    fun startConfirmActivity() {
        if(alarmManager == null) {
            nextWindow = true
            startActivity(Intent(this, ConfirmTimer::class.java).also{
                it.putExtra(currentRepeatsLeftLocation, repeats - currentrepeats)
            })
        }
    }

    fun Context.scheduleNotification() {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val timeInMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(/*currentSeconds.toLong()*/2L)

        with(alarmManager) {
            this?.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, getReceiver())
        }
    }

    private fun Context.getReceiver(): PendingIntent {
        return PendingIntent.getBroadcast(
            this,
            0,
            NotificationReceiver.build(this, this as StartedTimer),
            0
        )
    }

    @SuppressLint("SetTextI18n")
    private fun setTimerText(currentSeconds: Int){
        val seconds = ((currentSeconds % 86400) % 3600) % 60
        val minutes = ((currentSeconds % 86400) % 3600) / 60
        val hours = ((currentSeconds % 86400) / 3600)

        timerText.text =  "${String.format("%02d", hours)} : ${String.format("%02d", minutes)} : ${String.format("%02d", seconds)}"
    }

    fun getCurrentRepeatsLeft(): Int{
        return repeats - currentrepeats
    }
}