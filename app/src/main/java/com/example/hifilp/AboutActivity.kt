package com.example.hifilp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // ✅ Set up Toolbar with Back Button
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "About Our Team"
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ✅ Show back button

        // ✅ Handle back button click
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    // ✅ Inflate the menu (Loads main_menu.xml)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // ✅ Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {  // ✅ Handle Toolbar Back Button Click
                onBackPressed()
                true
            }
            R.id.aboutPage -> {
                // Stay on the same page
                true
            }
            R.id.teamDetails -> {
                // ✅ Navigate to Team Members Page
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            R.id.settings -> {
                // ✅ Navigate to SettingsActivity
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.exitApp -> {
                // ✅ Show exit confirmation dialog
                showExitConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // ✅ Show Exit Confirmation Dialog
    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit App")
        builder.setMessage("Are you sure you want to exit?")

        builder.setPositiveButton("Yes") { _, _ ->
            finishAffinity() // ✅ Closes all activities and exits the app
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss() // ✅ Dismisses the dialog
        }

        builder.setCancelable(false) // ✅ Prevents closing the dialog by tapping outside
        builder.show()
    }
}
