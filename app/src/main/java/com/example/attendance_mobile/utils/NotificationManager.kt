package com.example.attendance_mobile.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.attendance_mobile.R

class NotificationManager {
    companion object {
        fun showNotification(context : Context, notification: Notification) {
            with(NotificationManagerCompat.from(context)) {
                notify(getId(), notification)
            }
        }

        fun getId() : Int{
            return TimeUtils.getCurrentDateInInt()
        }

        fun buildNotification(context: Context,title : String, content : String) : NotificationCompat.Builder{
            return NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.circle)
                .setContentTitle(title)
                .setContentText(content)
                .setGroup("ATTENDANCE_NOTIFICATION_GROUP")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        fun buildForegroundNotification(context : Context,timeExecution : String) : Notification{
            return NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.notification_bg)
                .setContentTitle("Menunggu sesi berikutnya")
                .setContentText("KehadiranPerSesi selanjutnya akan dimulai dalam waktu $timeExecution menit")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build()
        }

        fun createNotificationChannel(context: Context,notificationManager: NotificationManager){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "CHANNEL_ID",
                    "APLIKASI KEHADIRAN BEACON",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "DESCRIPTION"
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}