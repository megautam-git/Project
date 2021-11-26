package com.example.project.services

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.project.CHANNEL_ID
import com.example.project.CHANNEL_NAME
import com.example.project.NOTIFICATION_ID
import com.example.project.R


class MyIntentService : IntentService("MusicIntentService") {
    lateinit var manager:NotificationManager
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    private fun createNotificationChannel() {

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel= NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

    }
    override fun onHandleIntent(intent: Intent?) {
        Toast.makeText(this, "service started", Toast.LENGTH_SHORT).show()
        mediaPlayer = MediaPlayer()

        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable {
                mediaPlayer.stop()
                Toast.makeText(this, "service stopped", Toast.LENGTH_SHORT).show()
            },
            120000

        )
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable {
                val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                    .setContentTitle("Service Notifiaction")
                    .setContentText("Music service stopped")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()
                //manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(NOTIFICATION_ID, notification)
            },120001
        )


    }
}