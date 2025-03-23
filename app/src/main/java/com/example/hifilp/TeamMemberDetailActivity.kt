package com.example.hifilp

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.IOException
import java.util.Locale

class TeamMemberDetailActivity : AppCompatActivity() {

    private lateinit var locationTextView: TextView
    private lateinit var latitudeInput: EditText
    private lateinit var longitudeInput: EditText
    private lateinit var fetchAddressButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_member_detail)

        val toolbar: Toolbar = findViewById(R.id.teamMemberToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Member Details"

        val teamMemberName = intent.getStringExtra("team_member_name")
        val teamMemberImage = intent.getIntExtra("team_member_image", 0)

        val nameTextView: TextView = findViewById(R.id.teamMemberName)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val teamMemberImageView: ImageView = findViewById(R.id.teamMemberImageView)
        val contactNumberTextView: TextView = findViewById(R.id.contactNumber)
        val emailAddressTextView: TextView = findViewById(R.id.emailAddress)
        locationTextView = findViewById(R.id.locationTextView)
        latitudeInput = findViewById(R.id.latitudeInput)
        longitudeInput = findViewById(R.id.longitudeInput)
        fetchAddressButton = findViewById(R.id.fetchAddressButton)

        nameTextView.text = teamMemberName
        if (teamMemberImage != 0) {
            teamMemberImageView.setImageResource(teamMemberImage)
        }

        val details = getFormattedDescription(teamMemberName)
        descriptionTextView.text = details.first
        descriptionTextView.movementMethod = LinkMovementMethod.getInstance()

        contactNumberTextView.text = "📞 Contact: ${details.second}"
        contactNumberTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${details.second}"))
            startActivity(intent)
        }

        emailAddressTextView.text = "📧 Email: ${details.third}"
        emailAddressTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${details.third}"))
            startActivity(intent)
        }

        // Button Click: Get Address from User Input and Show in Google Maps
        fetchAddressButton.setOnClickListener {
            val lat = latitudeInput.text.toString().toDoubleOrNull()
            val lng = longitudeInput.text.toString().toDoubleOrNull()

            if (lat != null && lng != null) {
                val newAddress = getAddressFromLocation(lat, lng)
                locationTextView.text = "📍 Entered Location: $newAddress (Lat: $lat, Lng: $lng)"

                // Open Google Maps with the entered coordinates
                val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng($newAddress)")
                val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            } else {
                Toast.makeText(this, "Enter valid latitude and longitude", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.team_member_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            R.id.menu_view_detail -> {
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getFormattedDescription(name: String?): Triple<SpannableString, String, String> {
        val details = when (name) {
            "Shanthini D" -> Triple(
                "Name: Shanthini D\nRoll No: 917722IT093\nDepartment: IT\nCollege: TCE\nYear: 3rd Year\nSkills: Front-end Dev, DBMS, UI/UX\nProjects: E-commerce App, Portfolio Website",
                "+91 9876543210",
                "shanthinid@student.tce.edu"
            )

            "Yasvitha R S" -> Triple(
                "Name: Yasvitha R S\nRoll No: 917722IT135\nDepartment: IT\nCollege: TCE\nYear: 3rd Year\nSkills: ML, AI, Full Stack Dev\nProjects: Chatbot, Expense Tracker",
                "+91 9876543211",
                "yasvitha@student.tce.edu"
            )

            "Rakesh S" -> Triple(
                "Name: Rakesh S\nRoll No: 917722IT075\nDepartment: IT\nCollege: TCE\nYear: 3rd Year\nSkills: AI, Machine Learning, Web Dev\nProjects: Resume Analyzer, Quiz Portal",
                "+91 9876543212",
                "rakeshs@student.tce.edu"
            )

            else -> Triple("No description available.", "", "")
        }

        return Triple(makeTextBold(details.first), details.second, details.third)
    }

    private fun makeTextBold(text: String): SpannableString {
        val spannable = SpannableString(text)
        val boldWords = listOf("Name:", "Roll No:", "Department:", "College:", "Year:", "Skills:", "Projects:")
        boldWords.forEach { word ->
            val startIndex = text.indexOf(word)
            if (startIndex >= 0) {
                spannable.setSpan(StyleSpan(Typeface.BOLD), startIndex, startIndex + word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return spannable
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.get(0)?.getAddressLine(0) ?: "Address not found"
        } catch (e: IOException) {
            "Address not found"
        }
    }
}