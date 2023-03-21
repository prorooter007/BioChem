package com.example.biochem


import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class TestName : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var button: Button
    val REQUEST_IMAGE_CAPTURE=100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_name)

        var text = intent.getStringExtra("test")
        val  textView_4 = findViewById<TextView>(R.id.textView4)
        textView_4.setText(text)

//
//        var text = intent.getStringExtra("key_1")
//
//        if (text != null){
//            textView_4.setText(text)
//        }
//
//        var text_2 = intent.getStringExtra("key_2")
//
//        if (text_2 != null){
//            textView_4.setText(text_2)
//        }
//
//        var text_3 = intent.getStringExtra("key_3")
//
//        if (text_3 != null){
//            textView_4.setText(text_3)
//        }
//
//        var text_4 = intent.getStringExtra("key_4")
//
//        if (text_4 != null){
//            textView_4.setText(text_4)
//        }
//
//        var text_5 = intent.getStringExtra("key_5")
//
//        if (text_5 != null){
//            textView_4.setText(text_5)
//        }
//
//        var text_6 = intent.getStringExtra("key_6")
//
//        if (text_6 != null){
//            textView_4.setText(text_6)
//        }


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

        button.setOnClickListener{
            val takePictureIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try{
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE)
            }catch(e:ActivityNotFoundException){
                Toast.makeText(this,"Error: "+e.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }

    imageView = findViewById(R.id.imageView6)
    button = findViewById(R.id.button8)

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