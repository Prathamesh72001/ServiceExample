package com.example.servicesexample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //hools
        start=findViewById(R.id.start)
        stop=findViewById(R.id.stop)
        reset=findViewById(R.id.reset)
        countTV=findViewById(R.id.countTV)

        //notification
        createNotificationChannel()

        //notificationIntents
        val notifyintent=Intent(this,MainActivity::class.java).apply {
            flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifypendingIntent=PendingIntent.getActivity(this,0,notifyintent,PendingIntent.FLAG_IMMUTABLE)

        val startIntent=Intent(this,NotificationReceiver::class.java)
        startIntent.putExtra("action","start")
        val startpendingIntent=PendingIntent.getBroadcast(this,1,startIntent,0)

        val stopIntent=Intent(this,NotificationReceiver::class.java)
        stopIntent.putExtra("action","stop")
        val stoppendingIntent=PendingIntent.getBroadcast(this,2,stopIntent,0)

        val resetIntent=Intent(this,NotificationReceiver::class.java)
        resetIntent.putExtra("action","reset")
        val resetpendingIntent=PendingIntent.getBroadcast(this,3,resetIntent,0)


        builder=NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.countdown)
            .setContentTitle("Current Count")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(notifypendingIntent)
            .setOnlyAlertOnce(true)
            .addAction(R.mipmap.ic_launcher,"Start",startpendingIntent)
            .addAction(R.mipmap.ic_launcher,"Stop",stoppendingIntent)
            .addAction(R.mipmap.ic_launcher,"Reset",resetpendingIntent)
            .setAutoCancel(true)


        //listeners
        start!!.setOnClickListener {
            startCount()
        }

        stop!!.setOnClickListener {
            stopCount()
        }

        reset!!.setOnClickListener {
            resetCount()
        }

        registerReceiver(updateCount, IntentFilter(MyService.UPDATED))
    }


    val updateCount:BroadcastReceiver=object :BroadcastReceiver(){
        override fun onReceive(context: Context, intent2: Intent) {
            count=intent2.getIntExtra(MyService.COUNT,0)
            countTV!!.text=count.toString()
            with(NotificationManagerCompat.from(applicationContext)){
                builder!!.setContentText(count.toString())
                notify(NOTIFICATION_ID,builder!!.build())
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val importance=NotificationManager.IMPORTANCE_DEFAULT
            val channel=NotificationChannel(CHANNEL_ID, CHANNEL_NAME,importance).apply {
                description= CHANNEL_DESC
            }

            notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager!!.createNotificationChannel(channel)
        }
    }

    fun resetCount() {
        myintent= Intent(applicationContext,MyService::class.java)
        countTV!!.text="0"
        count=0
        stopService(myintent)
        countTV!!.text="0"
        count=0
        with(NotificationManagerCompat.from(applicationContext)){
            builder!!.setContentText(count.toString())
            notify(NOTIFICATION_ID,builder!!.build())
        }
    }

    fun stopCount() {
        myintent= Intent(applicationContext,MyService::class.java)
        stopService(myintent)
    }

    fun startCount() {
        myintent= Intent(applicationContext,MyService::class.java)
        myintent!!.putExtra(MyService.COUNT,count)
        startService(myintent)
    }

    companion object{
        var count=0
        var myintent:Intent?=null
        var notificationManager:NotificationManager?=null
        const val CHANNEL_ID="MyChannelID"
        const val NOTIFICATION_ID=1
        var builder:NotificationCompat.Builder?=null
        const val CHANNEL_NAME="MyChannel"
        const val CHANNEL_DESC="MyChannelDesc"
        var start:Button?=null
        var stop:Button?=null
        var reset:Button?=null
        var countTV:TextView?=null
    }
}