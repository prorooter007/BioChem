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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val savedImageURL = MediaStore.Images.Media.insertImage(
                contentResolver,
                imageBitmap,
                "Image Title",
                "Image Description"
            )
            if (savedImageURL != null) {
                Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_LONG).show()
            }
            imageView.setImageBitmap(imageBitmap)

            val imageWidth = imageBitmap.width
            val imageHeight = imageBitmap.height
            val rgbValues = Array(imageHeight) { Array(imageWidth) { IntArray(3) } }

            for (i in 0 until imageHeight) {
                for (j in 0 until imageWidth) {
                    val pixel = imageBitmap.getPixel(j, i)
                    rgbValues[i][j][0] = (pixel shr 16) and 0xFF // red
                    rgbValues[i][j][1] = (pixel shr 8) and 0xFF  // green
                    rgbValues[i][j][2] = pixel and 0xFF         // blue
                }
            }
        }
    }
}
            // Now you can use the rgbValues variable as a 3D matrix to access the RGB values
            // for each pixel in the captured image.
    /** ok now run it*/
