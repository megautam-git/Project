package com.example.project.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.project.*
import android.os.Bundle





class MyService:Service() {

    lateinit var manager:NotificationManager
    lateinit var mediaPlayer:MediaPlayer
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
       createNotificationChannel()
    }

    private fun createNotificationChannel() {

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                val channel=NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT
                )
                manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
            }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Toast.makeText(this, "musis service started", Toast.LENGTH_SHORT).show()
        mediaPlayer= MediaPlayer()

        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        mediaPlayer.isLooping=true
        mediaPlayer.start()
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable {
                val broadcastIntent = Intent()
               // broadcastIntent.action = "com.example.project.string"
                broadcastIntent.action="custom-action-local-broadcast"
                broadcastIntent.putExtra("Data", "service stopped")
              //  sendBroadcast(broadcastIntent)
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
                /*val extras: Bundle = sbn.getNotification().extras
                text = extras.getCharSequence("android.text").toString()
                title = extras.getString("android.title")*/
              /*  intent?.putExtra("broadcastMessage", "echoMessage")?.let {
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(
                        it
                    )
                }*/
            },10000
        )
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "music service stopped", Toast.LENGTH_SHORT).show()
        mediaPlayer.stop()


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentTitle("Service Notifiaction")
            .setContentText("Music service stopped")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        manager.notify(NOTIFICATION_ID, notification)

    }
}