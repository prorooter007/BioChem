package com.example.biochem

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class SelectCam : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var button: Button
    val REQUEST_IMAGE_CAPTURE=100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_cam)

        val buttonMainActivit: Button = findViewById(
            R.id.button20
        )
        buttonMainActivit.setOnClickListener {
            val mainActivityIntent = Intent(
                applicationContext, TestName::class.java
            )
            startActivity(mainActivityIntent)
        }

        imageView = findViewById(R.id.image_save)
        button = findViewById(R.id.btn_take_picture)

        button.setOnClickListener{
            val takePictureIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try{
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }catch(e:ActivityNotFoundException){
                Toast.makeText(this,"Error: "+e.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode:Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
        else{
            super.onActivityResult(requestCode,resultCode, data)

        }
    }
}