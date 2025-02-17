package com.example.hifilp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class TimetableNotificationReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val subject = intent.getStringExtra("subject_name") ?: "Class Reminder"
        Log.d("TimetableNotification", "Received broadcast for: $subject")

        val notificationManager = NotificationManagerCompat.from(context)
        val channelId = "timetable_notifications"

        createNotificationChannel(context, channelId)

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, subject.hashCode(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Default system icon
            // Ensure this icon exists in res/drawable
            .setContentTitle("Class Reminder")
            .setContentText("Upcoming: $subject")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(subject.hashCode(), notification)
        Log.d("TimetableNotification", "Notification sent for: $subject")
    }

    private fun createNotificationChannel(context: Context, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Timetable Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for class schedule"
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Log.d("TimetableNotification", "Notification channel created: $channelId")
        }
    }
}
