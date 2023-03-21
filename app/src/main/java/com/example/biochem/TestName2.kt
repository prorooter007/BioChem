package com.example.biochem

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class TestName2 : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var button: Button
    val REQUEST_IMAGE_CAPTURE=100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_name2)

        var text = intent.getStringExtra("test")

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

        val testText: TextView = findViewById(R.id.textView2)

        testText.setText(text)


        button = findViewById(R.id.button9)

        button.setOnClickListener{
            val takePictureIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try{
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }catch(e: ActivityNotFoundException){
                Toast.makeText(this,"Error: "+e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }
}