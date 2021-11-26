package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnMusic.setOnClickListener {
            startActivity(Intent(this,MusicListActivity::class.java))
        }
        btnService.setOnClickListener {
            startActivity(Intent(this,ServiceActivity::class.java))
        }
        btnMaps.setOnClickListener {
            startActivity(Intent(this,MapActivity::class.java))
        }
    }
}