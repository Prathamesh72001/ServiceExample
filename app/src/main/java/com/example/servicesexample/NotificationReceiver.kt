package com.example.servicesexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class NotificationReceiver():BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action=intent.getStringExtra("action")
        Log.d("tag",action!!)
        when (action) {
            "start" -> {
                //MainActivity().startCount()
                myintent = Intent(context.applicationContext,MyService::class.java)
                myintent!!.putExtra(MyService.COUNT, MainActivity.count)
                context.applicationContext.startService(myintent)
            }
            "stop" -> {
                myintent = Intent(context.applicationContext,MyService::class.java)
                    context.applicationContext.stopService(MainActivity.myintent)
            }
            "reset" -> {
                myintent = Intent(context.applicationContext,MyService::class.java)
                MainActivity.countTV!!.text="0"
                MainActivity.count =0
                context.applicationContext.stopService(MainActivity.myintent)
                MainActivity.countTV!!.text="0"
                MainActivity.count =0
                with(NotificationManagerCompat.from(context.applicationContext)){
                    MainActivity.builder!!.setContentText(MainActivity.count.toString())
                    notify(MainActivity.NOTIFICATION_ID, MainActivity.builder!!.build())
                }
            }
        }
    }


    companion object{
        var myintent:Intent?=null
    }

}