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
    public var A_t = 0.0


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
                    val r = ByteArray(width * height * 3)
                    val g = ByteArray(width * height * 3)
                    val b = ByteArray(width * height * 3)

                    // Loop through the pixels of the Bitmap and store the RGB values in the 3D matrix
                    for (i in 0 until width) {
                        for (j in 0 until height) {
                            val pixel = bitmap.getPixel(i, j)
                            r[(j * width + i) ] = Color.red(pixel).toByte() // Red
                            g[(j * width + i) ] = Color.green(pixel).toByte() // Green
                            b[(j * width + i) ] = Color.blue(pixel).toByte() // Blue
                        }
                    }

                    val s_r = 1
                    val s_g = 1
                    val s_b = 1
                    val m = intent.getDoubleExtra("m", 0.0)
                    val c = intent.getDoubleExtra("c", 0.0)
                    val R_w = intent.getDoubleExtra("R_w", 0.0)
                    val G_w = intent.getDoubleExtra("G_w", 0.0)
                    val B_w = intent.getDoubleExtra("B_w", 0.0)

                    val numArray_r = r
                    var sum_r = 0.0

                    for (num_r in numArray_r) {
                        sum_r += num_r
                    }

                    val average_r = sum_r / numArray_r.size
                    // println("The average is: %.2f".format(average_r))

                    val numArray_g = g
                    var sum_g = 0.0

                    for (num_g in numArray_g) {
                        sum_g += num_g
                    }

                    val average_g = sum_g / numArray_g.size
                    // println("The average is: %.2f".format(average_g))


                    val numArray_b = b
                    var sum_b = 0.0

                    for (num_b in numArray_b) {
                        sum_b += num_b
                    }

                    val average_b = sum_b / numArray_b.size
                    // println("The average is: %.2f".format(average_b))


                    if (type == "Test"){
                        val  R_t = (average_r / (StrictMath.sqrt((average_r * average_r) + (average_g * average_g) + (average_b * average_b))))
                        val  G_t = (average_g / (StrictMath.sqrt((average_r * average_r) + (average_g * average_g) + (average_b * average_b))))
                        val  B_t = (average_b / (StrictMath.sqrt((average_r * average_r) + (average_g * average_g) + (average_b * average_b))))

                        A_t =  (s_r*R_t +s_g*G_t+s_b*B_t)/(s_r*R_w +s_g*G_w+s_b*B_w)
                    }

                    var x = A_t

                    var y = m*x + c

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
