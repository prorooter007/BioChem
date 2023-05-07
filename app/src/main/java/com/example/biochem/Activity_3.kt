package com.example.biochem

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
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
import java.io.FileOutputStream
import java.lang.StrictMath.min
import java.lang.StrictMath.sqrt
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
    private var camera: Camera? = null
    public var  R_w = 0.0
    public var  G_w = 0.0
    public var  B_w = 0.0
    public var A_b = 0.0
    public var A_std = 0.0


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
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT,
                Locale.US
            ).format(System.currentTimeMillis()) + "_" + type + ".jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: photoFile.toUri()
                    Log.d(TAG, "Photo saved: $savedUri")
                    Toast.makeText(this@Activity_3, "Photo saved", Toast.LENGTH_SHORT).show()

                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    val width = bitmap.width
                    val height = bitmap.height
                    
                            // RGB values of the image stored in the 3D matrix

                            val r = ByteArray(width * height * 3)
                            val g = ByteArray(width * height * 3)
                            val b = ByteArray(width * height * 3)
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



                    if (type == "Reagent Blank"){
                        val  R_b = (average_r / (sqrt((average_r*average_r) +(average_g*average_g)+ (average_b*average_b))))
                        val  G_b = (average_g / (sqrt((average_r*average_r) +(average_g*average_g)+ (average_b*average_b))))
                        val  B_b = (average_b / (sqrt((average_r*average_r) +(average_g*average_g)+ (average_b*average_b))))

                        A_b =  (s_r*R_b +s_g*G_b+s_b*B_b)/(s_r*R_w +s_g*G_w+s_b*B_w)
                    }
                    else if(type == "Standard"){
                        val  R_s = (average_r / (sqrt((average_r*average_r) +(average_g*average_g)+ (average_b*average_b))))
                        val  G_s = (average_g / (sqrt((average_r*average_r) +(average_g*average_g)+ (average_b*average_b))))
                        val  B_s = (average_b / (sqrt((average_r*average_r) +(average_g*average_g)+ (average_b*average_b))))

                         A_std = (s_r*R_s +s_g*G_s+s_b*B_s)/(s_r*R_w +s_g*G_w+s_b*B_w)
                    }
                    else if(type == "water"){
                          R_w = (average_r / (sqrt((average_r*average_r) +(average_g*average_g)+ (average_b*average_b))))
                          G_w = (average_g / (sqrt((average_r*average_r) +(average_g*average_g)+ (average_b*average_b))))
                          B_w = (average_b / (sqrt((average_r*average_r) +(average_g*average_g)+ (average_b*average_b))))
                    }

                    var x_1 = A_b
                    var x_2 = A_std
                    var y_1 =0.0
                    val editText = findViewById<EditText>(R.id.editText_y2)
                    val y_2 = editText.text.toString().toDouble()
                    var m = (y_2 - y_1)/(x_2 - x_1)

                    var c = y_2 - (m*x_2)

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

            // Set up the "Water" button
            val waterButton = findViewById<Button>(R.id.button_water)
            waterButton.setOnClickListener {
                takePhoto("Water")
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