package com.example.pbl_sns.service

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String){
        Log.d(TAG, "Refreshed token: $token")
    }

    // 앱이 전면에서 실행중일 때만 message 호출
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "From: ${message.from}")
        Log.d(TAG, "Message data payload: ${message.data}")
        val msgBody = message.notification?.body
        Log.d(TAG, "Message Notification Body: $msgBody")
    }

    companion object{
        const val TAG = "MyFirebaseMessaging"
    }
}