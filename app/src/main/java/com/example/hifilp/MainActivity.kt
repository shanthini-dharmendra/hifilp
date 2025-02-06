package com.example.hifilp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set click listeners for team member images
        findViewById<ImageView>(R.id.teamMember1).setOnClickListener { showPopupMenu(it, "Shanthini D", R.drawable.shanu) }
        findViewById<ImageView>(R.id.teamMember2).setOnClickListener { showPopupMenu(it, "Yasvitha R S", R.drawable.yash) }
        findViewById<ImageView>(R.id.teamMember3).setOnClickListener { showPopupMenu(it, "Rakesh S", R.drawable.rakesh) }
    }

    // Function to show popup menu on image click
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
}
