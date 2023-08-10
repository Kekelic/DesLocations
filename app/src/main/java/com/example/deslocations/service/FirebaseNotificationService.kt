package com.example.deslocations.service

import android.util.Log
import com.example.deslocations.R
import com.example.deslocations.presentation.notification.PlaceNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseNotificationService : FirebaseMessagingService() {


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.d("FirebaseService", message.data.toString())
        val placeNotification = PlaceNotification(
            context = this,
            title = message.data["title"]!!,
            text = this.resources.getString(R.string.new_post) + ": " + message.data["text"]!!,
            postID = message.data["postID"]!!
        )
        placeNotification.makeNotification()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}