package com.example.biochem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class nextpg2 : AppCompatActivity() {

    var value_1 = "URIC ACID TEST";
    var value_2 = "BILIRUBIN TEST";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nextpg2)

        val buttonMainActivit: Button = findViewById(
            R.id.button7
        )
        buttonMainActivit.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, TestName::class.java
            )
            mainActivityIntent.putExtra("key_1", value_1)
            startActivity(mainActivityIntent)


        }
        val buttonMainActivit2: Button = findViewById(
            R.id.button8
        )
        buttonMainActivit2.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, TestName::class.java
            )
            mainActivityIntent.putExtra("key_2", value_2)
            startActivity(mainActivityIntent)
        }
    }
}