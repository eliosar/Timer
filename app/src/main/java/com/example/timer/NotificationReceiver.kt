package com.example.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        context.showNotificationWithFullScreenIntent(intent.getIntExtra(StartedTimer::currentRepeatsLeftLocation.get(StartedTimer()), 0))
    }
    companion object {
        fun build(context: Context, startedTimer: StartedTimer): Intent {
            return Intent(context, ConfirmTimer::class.java).also{
                it.putExtra(startedTimer.currentRepeatsLeftLocation, startedTimer.getCurrentRepeatsLeft())
            }
        }
    }
}