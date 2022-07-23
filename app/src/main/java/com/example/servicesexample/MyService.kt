package com.example.servicesexample

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyService : Service() {
    var quit: Boolean = false

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("tag", "Start Service")
        val count = intent.getIntExtra(COUNT, 0)
        MyThread(count).start()
        return START_NOT_STICKY
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return true
    }

    override fun onRebind(intent: Intent?) {
        quit = true
        super.onRebind(intent)
    }


    override fun onDestroy() {
        quit = true
        super.onDestroy()
    }


    inner class MyThread(var count: Int) : Thread() {
        override fun run() {
            while (!quit) {
                try {
                    sleep(1000)
                } catch (e: Exception) {
                    Log.d("tag", e.message.toString())
                }
                val intent = Intent(UPDATED)
                count = count + 1
                intent.putExtra(COUNT, count)
                sendBroadcast(intent)
            }
        }
    }


    companion object {
        const val COUNT = "count"
        const val UPDATED = "update"
    }
}