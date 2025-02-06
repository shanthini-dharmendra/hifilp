package com.example.hifilp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class TimetableReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, TimetableNotificationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent) // ✅ Ensure service runs on Android 8+
        } else {
            context.startService(serviceIntent)
        }
    }
}
