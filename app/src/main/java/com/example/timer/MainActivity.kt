package com.example.timer

import android.annotation.SuppressLint
import android.app.usage.ExternalStorageStats
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {

    val LOG_TAG = "MY_ACTIVITY"

    val repeatMinutesLoc = "com.example.timer.repeatMinutes"
    val repeats = "com.example.timer.repeats"

    private lateinit var firstrepeatView: TextView
    private lateinit var secondrepeatView: TextView

    private lateinit var firstmaxView: TextView
    private lateinit var secondmaxView: TextView

    private var repeatMinutes: Int = 0
    private var maxMinutes: Int = 0
    private var currentSoundPath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(LOG_TAG, "startup")

        firstrepeatView = findViewById(R.id.firstrepeat)
        secondrepeatView = findViewById(R.id.secondrepeat)
        firstmaxView = findViewById(R.id.firstmax)
        secondmaxView = findViewById(R.id.secondmax)

        loadData()
        loadMusic()

        Log.d(LOG_TAG, "Data loaded")

        findViewById<EditText>(R.id.repeatMinutes).onFocusChangeListener = onFocusChangeListenerrepeat()

        findViewById<EditText>(R.id.maxMinutes).onFocusChangeListener = onFocusChangeListenermax()
    }

    fun setMusicView(view: View){
        findViewById<ScrollView>(R.id.scrollView).visibility = TextView.VISIBLE
        changeButtonsVisibility(TextView.GONE)
    }

    fun choseMusic(view: View){
        view as TextView
        findViewById<CardView>(R.id.scrollView).visibility = TextView.GONE
        changeButtonsVisibility(TextView.VISIBLE)
        currentSoundPath = view.hint.toString()
    }

    private fun changeButtonsVisibility(state: Int){
        findViewById<Button>(R.id.musicButton).visibility = state
        findViewById<Button>(R.id.timerbutton).visibility = state
    }

    @SuppressLint("Range", "InflateParams")
    private fun loadMusic(){
        val files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).listFiles()

        val musicContainer = layoutInflater.inflate(R.layout.container, null).findViewById<TextView>(R.id.MusicView)

        /*val recyclerListView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerListView.layoutManager = LinearLayoutManager(this)*/
        files.forEach { song ->
            if(song.length() > 4 && song.name.endsWith(".mp3")) {
                Log.d(LOG_TAG, "song detected: ${song.name}")

                musicContainer.text = "${song.nameWithoutExtension}"
                musicContainer.hint = "${song.path}"

                //recyclerListView.addView(musicContainer)
                musicContainer.hint.toString().also { currentSoundPath = it }
            }
        }
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

            putString("currentSoundPath", currentSoundPath)
        }.apply()
    }

    private fun loadData(){
        val sp = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        firstrepeatView.text = "${sp.getInt("firstrepeat", 0)}"
        secondrepeatView.text = "${sp.getInt("secondrepeat", 0)}"
        firstmaxView.text = "${sp.getInt("firstmax", 0)}"
        secondmaxView.text = "${sp.getInt("secondmax", 0)}"
        currentSoundPath = "${sp.getString("currentSoundPath", null)}"
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