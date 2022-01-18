package com.example.timer

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.graphics.drawable.toDrawable

class MainActivity : AppCompatActivity() {

    private val LOG_TAG = "MY_ACTIVITY"

    val repeatMinutes = "com.example.timer.repeatMinutes"
    val repeats = "com.example.timer.repeats"
    val logTag = "com.example.timer.Log_Tag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(LOG_TAG, "startup")
    }

    fun setTimerAction(view: View) {
        val repeatMinutesView = findViewById<EditText>(R.id.repeatMinutes)
        val maxMinutesView = findViewById<EditText>(R.id.maxMinutes)

        val repeatMinutes: Int = Integer.parseInt(repeatMinutesView.text.toString())
        val maxMinutes = Integer.parseInt(maxMinutesView.text.toString())

        val repeats: Int = maxMinutes / repeatMinutes
        Log.d(LOG_TAG, "$repeats = $maxMinutes / $repeatMinutes")

        val checkRight: Boolean = repeatMinutes * repeats == maxMinutes

        if (!checkRight) {
            Log.d(LOG_TAG, "false Input")
            repeatMinutesView.background = Color.RED.toDrawable()
            maxMinutesView.background = Color.RED.toDrawable()
        }else run {
            Log.d(LOG_TAG, "right Input")
            val intent = Intent(this, StartedTimer::class.java)
            intent.putExtra(this.repeatMinutes, repeatMinutes)
            intent.putExtra(this.repeats, repeats)
            intent.putExtra(this.logTag, LOG_TAG)
            Log.d(LOG_TAG, "new Activity")
            startActivity(intent)
            finish()
        }
        Log.d(LOG_TAG, "------------------")
    }
}