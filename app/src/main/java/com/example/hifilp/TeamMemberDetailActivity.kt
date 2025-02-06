package com.example.hifilp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class TeamMemberDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_member_detail)

        val toolbar: Toolbar = findViewById(R.id.teamMemberToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back button
        supportActionBar?.title = "Member Details"

        // Retrieve member details from intent
        val teamMemberName = intent.getStringExtra("team_member_name")
        val teamMemberImage = intent.getIntExtra("team_member_image", 0)

        val nameTextView: TextView = findViewById(R.id.teamMemberName)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val teamMemberImageView: ImageView = findViewById(R.id.teamMemberImageView)
        val contactNumberTextView: TextView = findViewById(R.id.contactNumber)
        val emailAddressTextView: TextView = findViewById(R.id.emailAddress)

        // Set Name and Image
        nameTextView.text = teamMemberName
        if (teamMemberImage != 0) {
            teamMemberImageView.setImageResource(teamMemberImage)
        }

        // Set Formatted Description
        val details = getFormattedDescription(teamMemberName)
        descriptionTextView.text = details.first
        descriptionTextView.movementMethod = LinkMovementMethod.getInstance() // Enable clickable spans

        // Set Contact Number (Clickable)
        contactNumberTextView.text = "📞 Contact: ${details.second}"
        contactNumberTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${details.second}"))
            startActivity(intent)
        }

        // Set Email Address (Clickable)
        emailAddressTextView.text = "📧 Email: ${details.third}"
        emailAddressTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${details.third}"))
            startActivity(intent)
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
                // Stay on the same page (or handle additional logic)
                true
            }
            android.R.id.home -> {
                finish() // Handle back button
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getFormattedDescription(name: String?): Triple<SpannableString, String, String> {
        val details = when (name) {
            "Shanthini D" -> Triple(
                """
                Name: Shanthini D
                Roll No: 917722IT093
                Department: Information Technology (IT)
                College: Thiagarajar College of Engineering (TCE)
                Year: 3rd Year

                Skills:
                - Front-end Development
                - Database Management (DBMS)
                - UI/UX Design
                
                Projects:
                - Built an E-commerce App UI using Flutter
                - Developed a Personal Portfolio Website
            """.trimIndent(),
                "+91 9876543210",
                "shanthinid@student.tce.edu"
            )

            "Yasvitha R S" -> Triple(
                """
                Name: Yasvitha R S
                Roll No: 917722IT135
                Department: Information Technology (IT)
                College: Thiagarajar College of Engineering (TCE)
                Year: 3rd Year

                Skills:
                - Machine Learning
                - Artificial Intelligence
                - Full Stack Development
                
                Projects:
                - Created a Chatbot using NLP
                - Developed a Web-based Expense Tracker
            """.trimIndent(),
                "+91 9876543211",
                "yasvitha@student.tce.edu"
            )

            "Rakesh S" -> Triple(
                """
                Name: Rakesh S
                Roll No: 917722IT075
                Department: Information Technology (IT)
                College: Thiagarajar College of Engineering (TCE)
                Year: 3rd Year

                Skills:
                - Machine Learning
                - AI
                - Full Stack Web Development
                
                Projects:
                - Developed an AI-Powered Resume Analyzer
                - Built an Online Quiz Portal
            """.trimIndent(),
                "+91 9876543212",
                "rakeshs@student.tce.edu"
            )

            else -> Triple("No description available.", "", "")
        }

        return Triple(makeTextBold(details.first), details.second, details.third)
    }

    private fun makeTextBold(text: String): SpannableString {
        val spannable = SpannableString(text)

        // Bold Labels
        val boldWords = listOf("Name:", "Roll No:", "Department:", "College:", "Year:", "Skills:", "Projects:")
        boldWords.forEach { word ->
            val startIndex = text.indexOf(word)
            if (startIndex >= 0) {
                spannable.setSpan(StyleSpan(Typeface.BOLD), startIndex, startIndex + word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        return spannable
    }
}
