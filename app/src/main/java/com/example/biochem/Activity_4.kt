package com.example.biochem

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class Activity_4 : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var button: Button
    val REQUEST_IMAGE_CAPTURE = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_4)

        var text = intent.getStringExtra("test")

        val buttonMainActivit: Button = findViewById(
            R.id.button10
        )
        buttonMainActivit.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, Activity_5::class.java
            )
            startActivity(mainActivityIntent)
        }

        val buttonMainActivity: Button = findViewById(
            R.id.button_back
        )
        buttonMainActivity.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, Activity_3::class.java
            )
            startActivity(mainActivityIntent)
        }

        val testText: TextView = findViewById(R.id.textView2)
        testText.setText(text)

        button = findViewById(R.id.button9)

        button.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Error: " + e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            saveImageToGallery(this, imageBitmap, "Biochem Image", "Biochem Image saved to Gallery")

            // Get the width and height of the bitmap
            val width = imageBitmap.width
            val height = imageBitmap.height

            // Create a 3D matrix to store the RGB values
            val rgbMatrix = Array(width) { Array(height) { IntArray(3) } }

            // Loop through the pixels of the bitmap and save the RGB values into the matrix
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val pixel = imageBitmap.getPixel(x, y)
                    rgbMatrix[x][y][0] = (pixel shr 16) and 0xFF // red value
                    rgbMatrix[x][y][1] = (pixel shr 8) and 0xFF // green value
                    rgbMatrix[x][y][2] = pixel and 0xFF // blue value
                }
            }
        }
    }


    private fun saveImageToGallery(context: Context, image: Bitmap, title: String, description: String) {
        val imageUri: Uri?
        var savedImageUri: Uri?
        val values = ContentValues()

        values.put(MediaStore.Images.Media.TITLE, title)
        values.put(MediaStore.Images.Media.DESCRIPTION, description)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())

        imageUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val outputStream = imageUri?.let { context.contentResolver.openOutputStream(it) }

        try {
            outputStream?.use {
                image.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            savedImageUri = imageUri
            Toast.makeText(context, "Image saved to Gallery", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            savedImageUri = null
            e.printStackTrace()
        }
    }
}
