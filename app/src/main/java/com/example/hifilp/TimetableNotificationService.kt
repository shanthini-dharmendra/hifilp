package com.example.hifilp

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*

class TimetableNotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sendTimetableNotification()
        return START_NOT_STICKY
    }

    private fun sendTimetableNotification() {
        val currentDay = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        val subject = TimeTableData.timeTable[currentDay]?.get(currentTime) ?: "No class scheduled"

        val subjectFullName = TimeTableData.subjectFullNames[subject] ?: subject

        val channelId = "timetable_notification"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Timetable Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("📚 Timetable Reminder")
            .setContentText("Current Class: $subjectFullName")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // ✅ Using built-in Android icon
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun onBind(intent: Intent?) = null
}
