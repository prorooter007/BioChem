package com.example.biochem

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Activity_3 : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    private val REQUEST_CODE_PERMISSIONS = 10
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        var text = intent.getStringExtra("test")
        val  textView_4 = findViewById<TextView>(R.id.textView4)
        textView_4.setText(text)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        fun getOutputDirectory(): File {
            val mediaDir = externalMediaDirs.firstOrNull()?.let {
                File(it, "CameraXApp").apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else filesDir
        }


        // Set up the output directory for saving images
        outputDirectory = getOutputDirectory()

        // Set up the camera executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Set up the preview view
        previewView = findViewById(R.id.imageView6)

        // Set up the "Standard" button
        val standardButton = findViewById<Button>(R.id.button8)
        standardButton.setOnClickListener {
            takePhoto("Standard")
        }

        // Set up the "Reagent Blank" button
        val blankButton = findViewById<Button>(R.id.button7)
        blankButton.setOnClickListener {
            takePhoto("Reagent Blank")
        }

        // Set up the "Next" button
        val nextButton = findViewById<Button>(R.id.button6)
        nextButton.setOnClickListener {
            // Add code to go to the next activity
            val secondActivityIntent = Intent(
                applicationContext, Activity_4::class.java
            )
            secondActivityIntent.putExtra("test", text)
            startActivity(secondActivityIntent)
        }
    }

    private fun takePhoto(type: String) {
        // Get a reference to the image capture use case
        val imageCapture = imageCapture ?: return

        // Create a timestamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT,
                Locale.US
            ).format(System.currentTimeMillis()) + "_" + type + ".jpg"
        )

        // Set up the output options object which contains the file and metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up the image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: photoFile.toUri()
                    Log.d(TAG, "Photo saved: $savedUri")
                    Toast.makeText(this@Activity_3, "Photo saved", Toast.LENGTH_SHORT).show()

                    // Get the Bitmap representation of the captured image
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

                    // Get the dimensions of the Bitmap
                    val width = bitmap.width
                    val height = bitmap.height

                    // Define a 3D matrix to hold the RGB values of the image
                    val rgbValues = ByteArray(width * height * 3)

                    // Loop through the pixels of the Bitmap and store the RGB values in the 3D matrix
                    for (i in 0 until width) {
                        for (j in 0 until height) {
                            val pixel = bitmap.getPixel(i, j)
                            rgbValues[(j * width + i) * 3] = Color.red(pixel).toByte() // Red
                            rgbValues[(j * width + i) * 3 + 1] = Color.green(pixel).toByte() // Green
                            rgbValues[(j * width + i) * 3 + 2] = Color.blue(pixel).toByte() // Blue
                        }
                    }

                    // RGB values of the image stored in the 3D matrix
                }

                override fun onError(exception: ImageCaptureException) {
                    val message = "Photo capture failed: ${exception.message}"
                    Log.e(TAG, message, exception)
                    Toast.makeText(this@Activity_3, message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Bind the preview and image capture use cases
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(previewView.display.rotation)
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind any previous use cases before binding new ones
                cameraProvider.unbindAll()

                // Bind the camera to the lifecycle of this activity
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exception: Exception) {
                Log.e(TAG, "Error starting camera: ${exception.message}", exception)
                Toast.makeText(this@Activity_3, "Error starting camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }
}
