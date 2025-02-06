package com.example.hifilp

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class SettingsActivity : AppCompatActivity() {  // ✅ Now inherits exit dialog behavior

    private lateinit var selectedDateTime1: TextView
    private lateinit var selectedDateTime2: TextView
    private lateinit var selectedDateTime3: TextView
    private var calendar: Calendar = Calendar.getInstance()
    private var selectedMemberId: Int = 0  // Track which member is selected

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)  // ✅ Ensure correct layout is loaded

        // ✅ Ensure layout is properly loaded before calling findViewById()
        initializeViews()

        // ✅ Request notification permission
        requestNotificationPermission()
    }

    private fun initializeViews() {
        selectedDateTime1 = findViewById(R.id.selectedDateTime1)
        selectedDateTime2 = findViewById(R.id.selectedDateTime2)
        selectedDateTime3 = findViewById(R.id.selectedDateTime3)

        val pickDateTime1: Button = findViewById(R.id.pickDateTime1)
        val pickDateTime2: Button = findViewById(R.id.pickDateTime2)
        val pickDateTime3: Button = findViewById(R.id.pickDateTime3)
        val setNotificationButton: Button = findViewById(R.id.setNotificationButton)

        // ✅ Click listeners for picking date & time
        pickDateTime1.setOnClickListener { showDateTimePicker(1) }
        pickDateTime2.setOnClickListener { showDateTimePicker(2) }
        pickDateTime3.setOnClickListener { showDateTimePicker(3) }

        // ✅ Save and set all notifications
        setNotificationButton.setOnClickListener { setAllNotifications() }
    }

    private fun showDateTimePicker(memberId: Int) {
        selectedMemberId = memberId

        val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
                calendar.set(year, month, dayOfMonth, hourOfDay, minute, 0)

                val selectedTextView = when (selectedMemberId) {
                    1 -> selectedDateTime1
                    2 -> selectedDateTime2
                    3 -> selectedDateTime3
                    else -> return@TimePickerDialog
                }
                selectedTextView.text = "Reminder set: $dayOfMonth/${month + 1}/$year $hourOfDay:$minute"

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
            timePicker.show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        datePicker.show()
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAllNotifications() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val memberTimes = listOf(
            selectedDateTime1.text.toString(),
            selectedDateTime2.text.toString(),
            selectedDateTime3.text.toString()
        )

        for (i in memberTimes.indices) {
            if (memberTimes[i].contains("Reminder set")) {
                val intent = Intent(this, NotificationReceiver::class.java)
                intent.putExtra("memberId", i + 1)

                val pendingIntent = PendingIntent.getBroadcast(
                    this, i + 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                // ✅ Ensure notifications are scheduled in the future
                if (calendar.timeInMillis <= System.currentTimeMillis()) {
                    calendar.add(Calendar.MINUTE, 1) // Add 1 minute if past time selected
                }

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }

        Toast.makeText(this, "All birthday reminders set!", Toast.LENGTH_SHORT).show()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }
}
