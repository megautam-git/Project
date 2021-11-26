package com.example.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project.services.MyService
import kotlinx.android.synthetic.main.activity_service.*

class ServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        btService.setOnClickListener {
            startActivity(Intent(this,MusicForegroundService::class.java))
        }
        btIntentService.setOnClickListener {
            startActivity(Intent(this,MusicIntentService::class.java))
        }
    }
}