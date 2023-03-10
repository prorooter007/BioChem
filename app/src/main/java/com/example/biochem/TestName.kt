package com.example.biochem


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class TestName : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_name)



        val buttonSecondActivity: Button = findViewById(
            R.id.button6
        )
        buttonSecondActivity.setOnClickListener {
            val secondActivityIntent = Intent(
                applicationContext, TestName2::class.java
            )
            startActivity(secondActivityIntent)
        }
    }
}