package com.example.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

fun Context.showNotificationWithFullScreenIntent(currentRepeatsLeft: Int) {
    Log.d("MY_ACTIVITY", "set builder")
    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.arrow_up_float)
        .setContentTitle("confirm timer")
        .setContentText("please confirm timer")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setFullScreenIntent(getFullScreenIntent(currentRepeatsLeft), true)


    Log.d("MY_ACTIVITY", "set notifyman")
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    with(notificationManager) {
        Log.d("MY_ACTIVITY", "build channel")
        buildChannel()

        Log.d("MY_ACTIVITY", "builder.build")
        val notification = builder.build()

        Log.d("MY_ACTIVITY", "notify")
        notify(0, notification)
    }
}

private fun NotificationManager.buildChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Log.d("MY_ACTIVITY", "set values for notify")
        val name = "Example Notification Channel"
        val descriptionText = "This is used to demonstrate the Full Screen Intent"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        Log.d("MY_ACTIVITY", "create Channel")
        createNotificationChannel(channel)
    }
}

private fun Context.getFullScreenIntent(currentRepeatsLeft: Int): PendingIntent {
    val intent = Intent(this, ConfirmTimer::class.java).also{
        it.putExtra(StartedTimer::currentRepeatsLeftLocation.get(StartedTimer()), currentRepeatsLeft)
    }
    Log.d("MY_ACTIVITY", "set intent Utils")

    return PendingIntent.getActivity(this, 0, intent, 0)
}

private val CHANNEL_ID = "channelId"