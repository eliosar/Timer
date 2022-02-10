package com.example.timer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ContentView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private val LOG_TAG = "MY_ACTIVITY"

    val repeatMinutesLoc = "com.example.timer.repeatMinutes"
    val repeats = "com.example.timer.repeats"
    val logTag = "com.example.timer.Log_Tag"

    private lateinit var firstrepeatView: TextView
    private lateinit var secondrepeatView: TextView

    private lateinit var firstmaxView: TextView
    private lateinit var secondmaxView: TextView

    private var repeatMinutes: Int = 0
    private var maxMinutes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(LOG_TAG, "startup")

        firstrepeatView = findViewById(R.id.firstrepeat)
        secondrepeatView = findViewById(R.id.secondrepeat)
        firstmaxView = findViewById(R.id.firstmax)
        secondmaxView = findViewById(R.id.secondmax)

        loadData()

        findViewById<EditText>(R.id.repeatMinutes).onFocusChangeListener = onFocusChangeListenerrepeat()

        findViewById<EditText>(R.id.maxMinutes).onFocusChangeListener = onFocusChangeListenermax()
    }

    fun setTimerAction(view: View) {
        val repeatMinutesView = findViewById<EditText>(R.id.repeatMinutes)
        val maxMinutesView = findViewById<EditText>(R.id.maxMinutes)

        repeatMinutes = Integer.parseInt(repeatMinutesView.text.toString())
        maxMinutes = Integer.parseInt(maxMinutesView.text.toString())

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
            intent.putExtra(repeatMinutesLoc, repeatMinutes)
            intent.putExtra(this.repeats, repeats)
            intent.putExtra(this.logTag, LOG_TAG)
            Log.d(LOG_TAG, "new Activity")

            saveData()

            startActivity(intent)
            finish()
        }
        Log.d(LOG_TAG, "------------------")
    }

    private fun saveData(){
        val sp = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sp.edit()

        editor.apply(){
            if(repeatMinutes != sp.getInt("firstrepeat", 0) && repeatMinutes != sp.getInt("secondrepeat", 0)) {
                putInt("firstrepeat", repeatMinutes)
                putInt("secondrepeat", sp.getInt("firstrepeat", 0))
            }

            if(maxMinutes != sp.getInt("firstmax", 0) && maxMinutes != sp.getInt("secondmax", 0)) {
                putInt("firstmax", maxMinutes)
                putInt("secondmax", sp.getInt("firstmax", 0))
            }
        }.apply()
    }

    private fun loadData(){
        val sp = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        firstrepeatView.text = "${sp.getInt("firstrepeat", 0)}"
        secondrepeatView.text = "${sp.getInt("secondrepeat", 0)}"
        firstmaxView.text = "${sp.getInt("firstmax", 0)}"
        secondmaxView.text = "${sp.getInt("secondmax", 0)}"

        Log.d(LOG_TAG, "Data loaded")
    }

    private fun onFocusChangeListenerrepeat(): View.OnFocusChangeListener{
        return View.OnFocusChangeListener(
            fun(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    firstrepeatView.visibility = TextView.VISIBLE
                    secondrepeatView.visibility = TextView.VISIBLE
                } else {
                    firstrepeatView.visibility = TextView.GONE
                    secondrepeatView.visibility = TextView.GONE
                }
            }
        )
    }

    private fun onFocusChangeListenermax(): View.OnFocusChangeListener{
        return View.OnFocusChangeListener(
            fun(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    firstmaxView.visibility = TextView.VISIBLE
                    secondmaxView.visibility = TextView.VISIBLE
                } else {
                    firstmaxView.visibility = TextView.GONE
                    secondmaxView.visibility = TextView.GONE
                }
            }
        )
    }

    fun repeatselected(viewnormal: View){
        val view = viewnormal as TextView
        val text = Editable.Factory.getInstance().newEditable(view.text)
        findViewById<EditText>(R.id.repeatMinutes).text = text
    }

    fun maxselected(viewnormal: View){
        val view = viewnormal as TextView
        val text = Editable.Factory.getInstance().newEditable(view.text)
        findViewById<EditText>(R.id.maxMinutes).text = text
    }
}