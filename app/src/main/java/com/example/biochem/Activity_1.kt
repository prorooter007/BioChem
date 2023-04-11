package com.example.biochem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Activity_1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        val buttonMainActivit: Button = findViewById(
            R.id.button
        )
        buttonMainActivit.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, Activity_2::class.java
            )
            startActivity(mainActivityIntent)
        }
    }
}