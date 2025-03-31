package com.example.hifilp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class SharedPrefActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvFavorites: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sharedpref)

        val etMindset = findViewById<EditText>(R.id.etMindset)
        val btnSaveMindset = findViewById<Button>(R.id.btnSaveMindset)
        val tvLastMindset = findViewById<TextView>(R.id.tvLastMindset)  // TextView to display last mindset

        val etFavorite = findViewById<EditText>(R.id.etFavorite)
        val etType = findViewById<EditText>(R.id.etType)
        val btnAddFavorite = findViewById<Button>(R.id.btnAddFavorite)
        val btnShowFavorites = findViewById<Button>(R.id.btnShowFavorites)
        tvFavorites = findViewById(R.id.tvFavorites)

        // Initialize database helper
        dbHelper = DatabaseHelper(this)

        // Load and display the last mindset
        val sharedPref = getSharedPreferences("MindsetPref", Context.MODE_PRIVATE)
        val lastMindset = sharedPref.getString("last_mindset", "No mindset saved")
        tvLastMindset.text = "Last Mindset: $lastMindset"

        // Save mindset using SharedPreferences
        btnSaveMindset.setOnClickListener {
            val mindset = etMindset.text.toString()
            with(sharedPref.edit()) {
                putString("last_mindset", mindset)
                apply()
            }
            tvLastMindset.text = "Last Mindset: $mindset"
            Toast.makeText(this, "Mindset Saved!", Toast.LENGTH_SHORT).show()
        }

        // Add favorite to SQLite
        btnAddFavorite.setOnClickListener {
            val name = etFavorite.text.toString()
            val type = etType.text.toString()

            if (name.isNotBlank() && type.isNotBlank()) {
                val db = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("name", name)
                    put("type", type)
                }
                db.insert("favorites", null, values)
                Toast.makeText(this, "Favorite Added!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter both name and type.", Toast.LENGTH_SHORT).show()
            }
        }

        // Show favorites from SQLite
        btnShowFavorites.setOnClickListener {
            displayFavorites()
        }
    }


    // Function to display favorites
    private fun displayFavorites() {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "favorites", null, null, null, null, null, null
        )
        val favorites = StringBuilder()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
            favorites.append("$type: $name\n")
        }
        cursor.close()
        tvFavorites.text = favorites.toString()
    }

    // SQLite Helper Class
    class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "FavoritesDB", null, 1) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE favorites (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, type TEXT)")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS favorites")
            onCreate(db)
        }
    }
}
