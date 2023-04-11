package com.example.biochem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Activity_2 : AppCompatActivity() {

    var value_1 = "URIC ACID TEST";
    var value_2 = "BILIRUBIN TEST";
    var value_3 = "GLUCOSE TEST";
    var value_4 = "CHOLESTEROL";
    var value_5 = "TOTAL PROTEIN";
    var value_6 = "TRIGLYCERIDES";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        val buttonMainActivit: Button = findViewById(
            R.id.button7
        )
        buttonMainActivit.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, Activity_3::class.java
            )
            mainActivityIntent.putExtra("test", value_1)
            startActivity(mainActivityIntent)


        }


        val buttonMainActivit2: Button = findViewById(
            R.id.button8
        )
        buttonMainActivit2.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, Activity_3::class.java
            )
            mainActivityIntent.putExtra("test", value_2)
            startActivity(mainActivityIntent)
        }


        val buttonMainActivit3: Button = findViewById(
            R.id.button9
        )
        buttonMainActivit3.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, Activity_3::class.java
            )
            mainActivityIntent.putExtra("test", value_3)
            startActivity(mainActivityIntent)
        }


        val buttonMainActivit4: Button = findViewById(
            R.id.button10
        )
        buttonMainActivit4.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, Activity_3::class.java
            )
            mainActivityIntent.putExtra("test", value_4)
            startActivity(mainActivityIntent)
        }

        val buttonMainActivit5: Button = findViewById(
            R.id.button11
        )
        buttonMainActivit5.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, Activity_3::class.java
            )
            mainActivityIntent.putExtra("test", value_5)
            startActivity(mainActivityIntent)
        }

        val buttonMainActivit6: Button = findViewById(
            R.id.button12
        )
        buttonMainActivit6.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, Activity_3::class.java
            )
            mainActivityIntent.putExtra("test", value_6)
            startActivity(mainActivityIntent)
        }
    }
}