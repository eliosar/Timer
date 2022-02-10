package com.example.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(p0: Context?, p1: Intent?) {
        val notibuilder = NotificationCompat.Builder(p0 as Context, "Notifications")
        val alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val pattern = longArrayOf(100, 200, 300, 400, 500)

        notibuilder.setSound(alarmsound)
        notibuilder.setVibrate(pattern)

        val intent = Intent(p0, ConfirmTimer::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val stackBuilder = TaskStackBuilder.create(p0)

        stackBuilder.addParentStack(ConfirmTimer::class.java)
        stackBuilder.addNextIntent(intent)

        val pendingintent = PendingIntent.getActivity(p0, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        notibuilder.setContentIntent(pendingintent)

        val channel = NotificationChannel("lol", "Timer finished", NotificationManager.IMPORTANCE_DEFAULT)

        val notifiManager = NotificationManagerCompat.from(p0)
        notifiManager.notify(321, notibuilder.build())
        notifiManager.createNotificationChannel(channel)
    }

}