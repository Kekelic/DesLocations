package com.example.deslocations.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.net.toUri
import com.example.deslocations.R
import com.example.deslocations.presentation.MainActivity
import kotlin.random.Random

const val MY_URI = "https://deslocations.com"
const val POST_ARG = "post_id"

class PlaceNotification(
    var context: Context,
    var title: String,
    var text: String,
    var postID: String
) {
    private val channelID: String = "FCM100"
    private val channelName: String = "FCMMessage"

    private val notificationManager =
        context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationBuilder: NotificationCompat.Builder

    fun makeNotification() {
        notificationChannel =
            NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "DesLocation channel for notifications"
                enableLights(true)
                lightColor = Color.GREEN
            }
        notificationManager.createNotificationChannel(notificationChannel)

        val intent = Intent(
            Intent.ACTION_VIEW,
            "$MY_URI/$POST_ARG=$postID".toUri(),
            context,
            MainActivity::class.java
        )
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE)
        }

        val notificationID = Random.nextInt()

        notificationBuilder = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVisibility(VISIBILITY_PUBLIC)
        notificationManager.notify(notificationID, notificationBuilder.build())
    }


}