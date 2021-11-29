package com.example.project

import android.app.NotificationChannel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.project.services.MyService
import kotlinx.android.synthetic.main.activity_music_foreground_service.*
import android.app.NotificationManager

import android.content.BroadcastReceiver


import android.content.Context
import android.content.IntentFilter
import android.os.Build
import androidx.localbroadcastmanager.content.LocalBroadcastManager




const val CHANNEL_ID = "notificationId"
const val CHANNEL_NAME = "notificationService"
const val NOTIFICATION_ID = 2
const val ACTION_LOCAL_BROADCAST = "com.example.LOCAL"

class MusicForegroundService : AppCompatActivity() {
    private var mLocalBroadcastManager: LocalBroadcastManager? = null
    val mBroadcastStringAction = "com.example.project.string"
    private var intentFilter:IntentFilter?=null
    lateinit var manager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_foreground_service)
        createNotificationChannel()
        intentFilter= IntentFilter()
        intentFilter?.addAction(mBroadcastStringAction)
        mLocalBroadcastManager= LocalBroadcastManager.getInstance(this)
        mLocalBroadcastManager?.registerReceiver(broadcastReceiver,intentFilter!!)
        btnStartService.setOnClickListener {

            val serviceIntent = Intent(this, MyService::class.java)
            startService(serviceIntent)

            callStopService()
        }

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,  IntentFilter("custom-action-local-broadcast"));
        //registerReceiver(broadcastReceiver,intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        //unregisterReceiver(broadcastReceiver)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    private fun callStopService() {
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable {
                stopService(Intent(baseContext, MyService::class.java))
                //registerReceiver(broadcastReceiver,intentFilter)
            }, 10000
        )
    }

    private var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action.equals("custom-action-local-broadcast")){

                 servicemsg.text=intent?.getStringExtra("Data")
            }
        }

    }

}