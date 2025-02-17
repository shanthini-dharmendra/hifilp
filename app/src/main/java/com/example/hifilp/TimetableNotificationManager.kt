package com.example.hifilp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.Calendar

object TimetableNotificationManager {

    fun scheduleDailyNotifications(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val timetable = TimeTableData.timeTable

        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_WEEK)
        val dayOfWeek = getDayOfWeek(today)

        timetable[dayOfWeek]?.forEach { (time, subject) ->
            scheduleHourlyNotification(context, time, subject, alarmManager)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleHourlyNotification(context: Context, time: String, subject: String, alarmManager: AlarmManager) {
        val (hour, minute) = time.split(":").map { it.toInt() }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If the time has already passed today, schedule for the next day
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        val intent = Intent(context, TimetableNotificationReceiver::class.java).apply {
            putExtra("subject_name", subject)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, time.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // ✅ Schedules notification every hour based on timetable
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_HOUR,
            pendingIntent
        )

        Log.d("TimetableNotification", "Scheduled: $subject at $time (Repeats every hour)")
    }

    private fun getDayOfWeek(day: Int): String {
        return when (day) {
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            Calendar.SUNDAY -> "Sunday"
            else -> "Monday"
        }
    }
}
