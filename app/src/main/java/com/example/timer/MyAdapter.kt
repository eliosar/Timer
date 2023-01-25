package com.example.timer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class MyAdapter constructor(context: Context?, songs: ArrayList<File>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private val context = context
    private val songs = songs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(com.example.timer.R.layout.container, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.songTextView.text = songs[position].nameWithoutExtension
        holder.songTextView.hint = songs[position].path
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        var songTextView: TextView

        init {
            songTextView = itemView.findViewById(com.example.timer.R.id.musicView)
            songTextView.setOnClickListener{MainActivity.choseMusic(songTextView.hint.toString())}
        }
    }
}