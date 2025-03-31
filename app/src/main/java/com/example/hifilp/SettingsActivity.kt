package com.example.hifilp

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var selectedDateTime1: TextView
    private lateinit var selectedDateTime2: TextView
    private lateinit var selectedDateTime3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize UI components
        initializeViews()

        // Request notification permission (for Android 13+)
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

        // Date & time picker for each member
        pickDateTime1.setOnClickListener { showDateTimePicker(1) }
        pickDateTime2.setOnClickListener { showDateTimePicker(2) }
        pickDateTime3.setOnClickListener { showDateTimePicker(3) }

        // Set all notifications
        setNotificationButton.setOnClickListener { setAllNotifications() }
    }

    private fun showDateTimePicker(memberId: Int) {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->

                val selectedCalendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, hourOfDay, minute, 0)
                }

                val dateTimeString = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(selectedCalendar.time)

                when (memberId) {
                    1 -> selectedDateTime1.text = "Reminder set: $dateTimeString"
                    2 -> selectedDateTime2.text = "Reminder set: $dateTimeString"
                    3 -> selectedDateTime3.text = "Reminder set: $dateTimeString"
                }

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
                val dateTimeString = memberTimes[i].replace("Reminder set: ", "")

                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

                try {
                    calendar.time = dateFormat.parse(dateTimeString)!!

                    if (calendar.timeInMillis <= System.currentTimeMillis()) {
                        calendar.add(Calendar.MINUTE, 1) // Ensure future time
                    }

                    val intent = Intent(this, NotificationReceiver::class.java).apply {
                        putExtra("memberId", i + 1)
                    }

                    val pendingIntent = PendingIntent.getBroadcast(
                        this, i + 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    // Use setExactAndAllowWhileIdle() to wake up the device at the exact time
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

                    Log.d("BirthdayReminder", "Reminder set for Member ${i + 1} at ${calendar.time}")

                    // Save the reminder to Firebase Firestore
                    saveReminderToFirestore(i + 1, dateTimeString)

                } catch (e: Exception) {
                    Log.e("BirthdayReminder", "Error parsing date: ${e.message}")
                }
            }
        }

        Toast.makeText(this, "All birthday reminders set!", Toast.LENGTH_SHORT).show()
    }

    /**
     * Save reminder to Firebase Firestore
     */
    private fun saveReminderToFirestore(memberId: Int, dateTimeString: String) {
        val db = FirebaseFirestore.getInstance()
        val reminderData = hashMapOf(
            "memberId" to memberId,
            "dateTime" to dateTimeString
        )

        db.collection("BirthdayReminders")
            .add(reminderData)
            .addOnSuccessListener {
                Log.d("BirthdayReminder", "Reminder saved for Member $memberId at $dateTimeString")
                Toast.makeText(this, "Birthday reminder saved to Firebase!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("BirthdayReminder", "Error saving reminder: ${e.message}")
                Toast.makeText(this, "Failed to save reminder in Firebase.", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Request notification permission (Required for Android 13+)
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_CODE)
            }
        }
    }

    /**
     * Handle the result of the permission request
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied. Notifications will not work!", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Define a constant for permission request code
     */
    companion object {
        private const val NOTIFICATION_PERMISSION_CODE = 1001
    }
}