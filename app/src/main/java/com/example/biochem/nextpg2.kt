package com.example.biochem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class nextpg2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nextpg2)

        val buttonMainActivit: Button = findViewById(
            R.id.button7
        )
        buttonMainActivit.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, SelectCam::class.java
            )
            startActivity(mainActivityIntent)
        }
    }
}