package com.example.biochem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class TestName2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_name2)

        val buttonMainActivit: Button = findViewById(
            R.id.button10
        )
        buttonMainActivit.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, result::class.java
            )
            startActivity(mainActivityIntent)
        }


        val buttonMainActivity: Button = findViewById(
            R.id.button_back
        )
        buttonMainActivity.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, TestName::class.java
            )
            startActivity(mainActivityIntent)
        }




    }
}