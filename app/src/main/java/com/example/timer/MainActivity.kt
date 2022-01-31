package com.example.timer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private val LOG_TAG = "MY_ACTIVITY"

    val repeatMinutes = "com.example.timer.repeatMinutes"
    val repeats = "com.example.timer.repeats"
    val logTag = "com.example.timer.Log_Tag"

    private lateinit var ll: ConstraintLayout

    private lateinit var firstrepeatView: TextView
    private lateinit var secondrepeatView: TextView

    private lateinit var firstmaxView: TextView
    private lateinit var secondmaxView: TextView

    private val ViewSize = 15f

    private var repeatViewindex = 0
    private var maxViewindex = 0

    private var firstrep = 0
    private var firstmax = 0
    private var secondrep = 0
    private var secondmax = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(LOG_TAG, "startup")
        /*loadData()

        ll = findViewById(R.id.parent_linear_Layout)

        loadrepeatView()
        loadmaxView()

        findViewById<EditText>(R.id.repeatMinutes).onFocusChangeListener = View.OnFocusChangeListener(
            fun(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    ll.addView(firstrepeatView, repeatViewindex)
                    ll.addView(secondrepeatView, repeatViewindex)
                } else {
                    ll.removeView(firstrepeatView)
                    ll.removeView(secondrepeatView)
                }
            }
        )

        findViewById<EditText>(R.id.maxMinutes).onFocusChangeListener = View.OnFocusChangeListener(
            fun(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    ll.addView(firstmaxView, maxViewindex)
                    ll.addView(secondmaxView, maxViewindex)
                } else {
                    ll.removeView(firstmaxView)
                    ll.removeView(secondmaxView)
                }
            }
        )*/
    }

    fun setTimerAction(view: View) {
        val repeatMinutesView = findViewById<EditText>(R.id.repeatMinutes)
        val maxMinutesView = findViewById<EditText>(R.id.maxMinutes)

        val repeatMinutes: Int = Integer.parseInt(repeatMinutesView.text.toString())
        val maxMinutes = Integer.parseInt(maxMinutesView.text.toString())

        val repeats: Int = maxMinutes / repeatMinutes
        Log.d(LOG_TAG, "$repeats = $maxMinutes / $repeatMinutes")

        val checkRight: Boolean = (repeatMinutes * repeats == maxMinutes) && repeatMinutes != 0 && repeats != 0

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

            val sp = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val editor = sp.edit()

            editor.apply(){
                putInt("secondrepeat", sp.getInt("firstrepeat", 0))
                putInt("secondmax", sp.getInt("firstmax", 0))

                putInt("firstrepeat", repeatMinutes)
                putInt("firstmax", maxMinutes)
            }.apply()

            startActivity(intent)
            finish()
        }
        Log.d(LOG_TAG, "------------------")
    }

    private fun loadData(){
        val sp = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        firstrep = sp.getInt("firstrepeat", 0)
        firstmax = sp.getInt("firstmax", 0)
        secondrep = sp.getInt("secondrepeat", 0)
        secondmax = sp.getInt("secondmax", 0)

        Log.d(LOG_TAG, "Data loaded")
    }

    private fun loadrepeatView(){
        Thread {
            val currentview = ll.getChildAt(repeatViewindex)
            while (currentview != findViewById(R.id.repeatMinutes)) {
                repeatViewindex++

                ll.getChildAt(repeatViewindex)
            }
        }

        val repeatView = findViewById<EditText>(R.id.repeatMinutes)

        firstrepeatView.layoutParams = repeatView.layoutParams
        Log.d(LOG_TAG, "params")
        firstrepeatView.textSize = ViewSize
        Log.d(LOG_TAG, "size")
        firstrepeatView.text = "$firstrep"
        Log.d(LOG_TAG, "text")

        secondrepeatView.layoutParams = repeatView.layoutParams
        secondrepeatView.textSize = ViewSize
        secondrepeatView.text = "$secondrep"
        Log.d(LOG_TAG, "lol")
    }

    private fun loadmaxView(){
        Thread {
            val currentview = ll.getChildAt(maxViewindex)
            while (currentview != findViewById(R.id.maxMinutes)) {
                maxViewindex++

                ll.getChildAt(maxViewindex)
            }
        }

        val maxView = findViewById<EditText>(R.id.maxMinutes)

        firstmaxView.layoutParams = maxView.layoutParams
        firstmaxView.textSize = ViewSize
        firstmaxView.text = "$firstmax"

        secondmaxView.layoutParams = maxView.layoutParams
        secondmaxView.textSize = ViewSize
        secondmaxView.text = "$secondmax"
    }
}