package com.example.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class StartedTimer : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_started_timer)

        val repeatMinutes = intent.getStringExtra(MainActivity::repeatMinutes.toString())
        val repeats = intent.getStringExtra(MainActivity::repeats.toString())
        val log_Tag = intent.getStringExtra(MainActivity::logTag.toString())

        Log.d(log_Tag, "repeatMinutes: $repeatMinutes")
        Log.d(log_Tag, "repeats: $repeats")
    }
}