package com.example.timer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {

    val repeatMinutesLoc = "com.example.timer.repeatMinutes"
    val repeats = "com.example.timer.repeats"

    private lateinit var firstrepeatView: TextView
    private lateinit var secondrepeatView: TextView

    private lateinit var firstmaxView: TextView
    private lateinit var secondmaxView: TextView

    private lateinit var repeatText: TextView
    private lateinit var maxText: TextView

    private var repeatMinutes = 0
    private var maxMinutes = 0

    companion object{
        val LOG_TAG = "MY_ACTIVITY"
        var currentSoundPath = ""
        private var songsFound = 0
        lateinit var recyclerView: RecyclerView
        @SuppressLint("StaticFieldLeak")
        lateinit var musicButton: Button
        @SuppressLint("StaticFieldLeak")
        lateinit var timerButton: Button

        fun setMusicView(){
            if(songsFound > 0) {
                recyclerView.visibility = TextView.VISIBLE
                changeButtonsVisibility(TextView.GONE)
            }
        }

        fun choseMusic(path: String) {
            Log.d("MY_AC", "music $path chosen")
            recyclerView.visibility = TextView.GONE
            changeButtonsVisibility(TextView.VISIBLE)
            currentSoundPath = path
        }

        private fun changeButtonsVisibility(state: Int){
            musicButton.visibility = state
            timerButton.visibility = state
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(LOG_TAG, "startup")

        firstrepeatView = findViewById(R.id.firstrepeat)
        secondrepeatView = findViewById(R.id.secondrepeat)
        firstmaxView = findViewById(R.id.firstmax)
        secondmaxView = findViewById(R.id.secondmax)
        recyclerView = findViewById(R.id.recyclerView)
        musicButton = findViewById(R.id.musicButton)
        timerButton = findViewById(R.id.timerbutton)
        repeatText = findViewById<EditText>(R.id.repeatMinutes)
        maxText = findViewById<EditText>(R.id.maxMinutes)

        loadData()
        loadMusic()

        Log.d(LOG_TAG, "Data loaded")

        repeatText.onFocusChangeListener = onFocusChangeListenerRepeat()
        maxText.onFocusChangeListener = onFocusChangeListenerMax()
    }

    fun setMusicView(view: View){
        setMusicView()
    }

    @SuppressLint("InflateParams")
    private fun loadMusic(){
        val files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).listFiles()

        val songs : ArrayList<File> = ArrayList()

        if(files.isNotEmpty()){
            files?.forEach { song ->
                if(song.length() > 4 && song.name.endsWith(".mp3")) {
                    Log.d(LOG_TAG, "song detected: ${song.nameWithoutExtension}")
                    songs.add(song)
                }
            }
        }

        songsFound = songs.size

        val recyclerListView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerListView.layoutManager = LinearLayoutManager(this)
        recyclerListView.adapter = MyAdapter(applicationContext, songs)
    }

    fun setTimerAction(view: View) {
        val repeatMinutesView = repeatText
        val maxMinutesView = maxText
        var checkRight = true
        var repeats = 0

        try {
            repeatMinutes = Integer.parseInt(repeatMinutesView.text.toString())
            maxMinutes = Integer.parseInt(maxMinutesView.text.toString())

            repeats = maxMinutes / repeatMinutes
            Log.d(LOG_TAG, "$repeats = $maxMinutes / $repeatMinutes")

            checkRight = ((repeatMinutes * repeats == maxMinutes) && repeatMinutes != 0 && repeats != 0) && currentSoundPath.isNotEmpty()
        }catch (e: java.lang.Exception){
            checkRight = false
        }

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

        editor.apply{
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

    private fun onFocusChangeListenerRepeat(): View.OnFocusChangeListener{
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

    private fun onFocusChangeListenerMax(): View.OnFocusChangeListener{
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

    fun repeatSelected(view: View){
        view as TextView
        repeatText.text = Editable.Factory.getInstance().newEditable(view.text)
    }

    fun maxSelected(view: View){
        view as TextView
        maxText.text = Editable.Factory.getInstance().newEditable(view.text)
    }
}