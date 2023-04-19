package com.example.biochem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Activity_5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_5)

        // Set up the "Back" button
        val backButton = findViewById<Button>(R.id.button_back)
        backButton.setOnClickListener {
            // Add code to go to the previous activity
            val secondActivityIntent = Intent(
                applicationContext, Activity_4::class.java
            )

            startActivity(secondActivityIntent)
        }

    }
}