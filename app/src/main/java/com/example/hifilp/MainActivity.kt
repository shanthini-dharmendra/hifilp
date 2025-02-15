package com.example.hifilp

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ Request notification permissions for Android 13+
        requestNotificationPermission()

        // ✅ Check & Request Exact Alarm Permission (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(AlarmManager::class.java)
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Please enable Exact Alarms in settings!", Toast.LENGTH_LONG).show()
                openExactAlarmSettings()
            }
        }

        // ✅ Schedule hourly notifications
        scheduleHourlyTimetableNotifications()

        // ✅ Set click listeners for team member images
        findViewById<ImageView>(R.id.teamMember1).setOnClickListener { showPopupMenu(it, "Shanthini D", R.drawable.shanu) }
        findViewById<ImageView>(R.id.teamMember2).setOnClickListener { showPopupMenu(it, "Yasvitha R S", R.drawable.yash) }
        findViewById<ImageView>(R.id.teamMember3).setOnClickListener { showPopupMenu(it, "Rakesh S", R.drawable.rakesh) }
    }

    // ✅ Function to show the popup menu on image click
    private fun showPopupMenu(view: View, name: String, imageRes: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.viewDetails -> {
                    val intent = Intent(this, TeamMemberDetailActivity::class.java)
                    intent.putExtra("team_member_name", name)
                    intent.putExtra("team_member_image", imageRes)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    // ✅ Schedule Hourly Notifications for Timetable
    private fun scheduleHourlyTimetableNotifications() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, TimetableReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 1001, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.MINUTE, 0)
            add(Calendar.HOUR, 1) // Start from the next hour
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_HOUR,
                pendingIntent
            )
        }

        Toast.makeText(this, "Timetable notifications enabled!", Toast.LENGTH_SHORT).show()
    }

    // ✅ Request notification permission (Android 13+)
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

    // ✅ Open exact alarm settings for Android 12+
    private fun openExactAlarmSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.data = android.net.Uri.parse("package:$packageName")
            startActivity(intent)
        }
    }
}
