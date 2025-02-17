package com.example.hifilp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)

        requestNotificationPermission()
        checkExactAlarmPermission()

        // ✅ Schedule timetable notifications once during app launch
        if (checkNotificationPermission()) {
            TimetableNotificationManager.scheduleDailyNotifications(this)
            scheduleBirthdayReminders()
            Log.d("TimetableNotification", "Notifications scheduled successfully")
        } else {
            Log.e("TimetableNotification", "Notification permission not granted!")
        }

        findViewById<ImageView>(R.id.teamMember1).setOnClickListener { showPopupMenu(it, "Shanthini D", R.drawable.shanu) }
        findViewById<ImageView>(R.id.teamMember2).setOnClickListener { showPopupMenu(it, "Yasvitha R S", R.drawable.yash) }
        findViewById<ImageView>(R.id.teamMember3).setOnClickListener { showPopupMenu(it, "Rakesh S", R.drawable.rakesh) }
    }

    private fun showPopupMenu(view: View, name: String, imageRes: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.viewDetails -> {
                    showProgressBar()
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, TeamMemberDetailActivity::class.java)
                        intent.putExtra("team_member_name", name)
                        intent.putExtra("team_member_image", imageRes)
                        startActivity(intent)
                        hideProgressBar()
                    }, 1000)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    /**
     * Request notification permission for Android 13+ (API 33+)
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

    /**
     * Check if notification permission is granted
     */
    private fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * Request exact alarm permission for Android 12+ (API 31+)
     */
    private fun checkExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(AlarmManager::class.java)
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Please enable Exact Alarms in settings!", Toast.LENGTH_LONG).show()
                openExactAlarmSettings()
            }
        }
    }

    private fun openExactAlarmSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.data = android.net.Uri.parse("package:$packageName")
            startActivity(intent)
        }
    }

    /**
     * Schedule yearly birthday reminders
     */
    private fun scheduleBirthdayReminders() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val birthdays = listOf(
            Triple(1, "Shanthini D", Calendar.getInstance().apply { set(Calendar.MONTH, Calendar.MARCH); set(Calendar.DAY_OF_MONTH, 25) }),
            Triple(2, "Yasvitha R S", Calendar.getInstance().apply { set(Calendar.MONTH, Calendar.APRIL); set(Calendar.DAY_OF_MONTH, 10) }),
            Triple(3, "Rakesh S", Calendar.getInstance().apply { set(Calendar.MONTH, Calendar.MAY); set(Calendar.DAY_OF_MONTH, 5) })
        )

        for ((id, name, calendar) in birthdays) {
            calendar.set(Calendar.HOUR_OF_DAY, 9) // Reminder at 9 AM
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val currentTime = System.currentTimeMillis()

            // 🔄 Ensure the reminder is scheduled for the next occurrence
            if (calendar.timeInMillis < currentTime) {
                calendar.add(Calendar.YEAR, 1) // Move to next year
            }

            val intent = Intent(this, NotificationReceiver::class.java).apply {
                putExtra("memberId", id)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // ✅ Schedule yearly repeating alarm
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

            Log.d("BirthdayReminder", "Scheduled $name's birthday reminder for ${calendar.time}")
        }
    }
}
