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
            R.id.aboutTeam -> {  // ✅ When "About Team" is clicked
                val intent = Intent(this, AboutTeamActivity::class.java)
                startActivity(intent)  // ✅ Navigate to About Team Page
                true
            }
            R.id.projectDescription -> {
                val intent = Intent(this, ProjectDescriptionActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.aboutPage -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.teamDetails -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
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
