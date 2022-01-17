package com.example.timer

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import java.util.*

class StartedTimer : AppCompatActivity() {

    private var repeatMinutes: Int = 0
    private var repeats: Int = 0
    private var LOG_TAG: String? = ""
    private var togoinpercent = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_started_timer)

        repeatMinutes = intent.getIntExtra(MainActivity::repeatMinutes.get(MainActivity()), repeatMinutes)
        repeats = intent.getIntExtra(MainActivity::repeats.get(MainActivity()), repeats)
        LOG_TAG = intent.getStringExtra(MainActivity::logTag.get(MainActivity()))
    }

    private fun progress(){
        val pb = findViewById<ProgressBar>(R.id.timeprogressline)


    }
}