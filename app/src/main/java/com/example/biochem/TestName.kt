package com.example.biochem


import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class TestName : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var button: Button
    val REQUEST_IMAGE_CAPTURE = 1222


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_name)

        var text = intent.getStringExtra("test")
        val textView_4 = findViewById<TextView>(R.id.textView4)
        textView_4.setText(text)

        val buttonSecondActivity: Button = findViewById(
            R.id.button6
        )
        buttonSecondActivity.setOnClickListener {
            val secondActivityIntent = Intent(
                applicationContext, TestName2::class.java
            )
            secondActivityIntent.putExtra("test", text)
            startActivity(secondActivityIntent)
        }

        imageView = findViewById(R.id.imageView6)
        button = findViewById(R.id.button7)

        /**set camera Open*/
        button.setOnClickListener {
            val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraInt, REQUEST_IMAGE_CAPTURE)
        }

        imageView = findViewById(R.id.imageView6)
        button = findViewById(R.id.button8)

        /**get Permission*/
        if (ContextCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        )
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_IMAGE_CAPTURE
            )
        /**set camera Open*/
        button.setOnClickListener {
            val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraInt, REQUEST_IMAGE_CAPTURE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            /**save to Image In layout*/
            val images: Bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(images)
        }
    }
    /** ok now run it*/

}
