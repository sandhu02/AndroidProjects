package com.example.edtech.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

fun sendChatNotification(context: Context , notificationTitle:String , notificationBody:String){
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channelId = "notification_channel"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId, "Test Notifications", NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle(notificationTitle)
        .setContentText(notificationBody)
        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
        .build()

    notificationManager.notify(1, notification)
}