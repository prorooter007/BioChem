package com.example.biochem

import android.annotation.SuppressLint
import android.content.Intent
//import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.view.View
import android.widget.*

class Activity_1 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        val items = arrayListOf("URIC ACID TEST","BILIRUBIN TEST","GLUCOSE TEST","CHOLESTEROL","TOTAL PROTEIN")
        val autoComplete : AutoCompleteTextView = findViewById(R.id.drop_down_main)

        val adapter=ArrayAdapter(this,R.layout.list_item, items)
        autoComplete.setAdapter(adapter)

        autoComplete.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->
            val itemSelected = adapterView.getItemAtPosition(i).toString()
            Toast.makeText(this,"Item: $itemSelected", Toast.LENGTH_SHORT).show()
            val mainActivityIntent = Intent(
                applicationContext, Activity_3::class.java
            )
            mainActivityIntent.putExtra("test", itemSelected)
            startActivity(mainActivityIntent)
        }

        autoComplete.setOnClickListener {
//            val mainActivityIntent = Intent(
//                applicationContext, TestName::class.java
//            )
//            mainActivityIntent.putStringArrayListExtra("test", items)
//            startActivity(mainActivityIntent)
        }
    }
}