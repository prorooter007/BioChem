package com.example.biochem

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
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


class Activity_4 : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    private val REQUEST_CODE_PERMISSIONS = 10
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    private var camera: Camera? = null


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_4)

        var text = intent.getStringExtra("test")
        val  textView_42 = findViewById<TextView>(R.id.textView4_2)
        textView_42.setText(text)

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
        previewView = findViewById(R.id.imageView6_2)


        // Set up the "Back" button
        val backButton = findViewById<Button>(R.id.button6_2)
        backButton.setOnClickListener {
            // Add code to go to the previous activity
            val secondActivityIntent = Intent(
                applicationContext, Activity_3::class.java
            )

            startActivity(secondActivityIntent)
        }

        // Set up the "Result" button
        val nextButton = findViewById<Button>(R.id.button8_2)
        nextButton.setOnClickListener {
            // Add code to go to the previous activity
            val secondActivityIntent = Intent(
                applicationContext, Activity_5::class.java
            )

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
                    Log.d(ContentValues.TAG, "Photo saved: $savedUri")
                    Toast.makeText(this@Activity_4, "Photo saved", Toast.LENGTH_SHORT).show()

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
                    Log.e(ContentValues.TAG, message, exception)
                    Toast.makeText(this@Activity_4, message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({

            // Set up the "Reagent Blank" button
            val blankButton = findViewById<Button>(R.id.button7_2)
            blankButton.setOnClickListener {
                takePhoto("Test")
            }

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

            // Set up the camera control
            val cameraControl = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            ).cameraControl

            // Bind the camera to the lifecycle
            val camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

            // Get the camera info
            val cameraInfo = camera.cameraInfo

            // Set up the zoom slider
            //val cameraInfo = cameraProvider.getCameraInfo(cameraSelector)
            val zoomSeekBar = findViewById<SeekBar>(R.id.seekBar)
            zoomSeekBar.max = cameraInfo.zoomState.value?.maxZoomRatio?.times(10)?.toInt() ?: 0
            zoomSeekBar.progress = 0
            zoomSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    val zoomRatio = progress.toFloat() / 10f
                    cameraControl.setZoomRatio(zoomRatio)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

        }, ContextCompat.getMainExecutor(this))
    }
}
