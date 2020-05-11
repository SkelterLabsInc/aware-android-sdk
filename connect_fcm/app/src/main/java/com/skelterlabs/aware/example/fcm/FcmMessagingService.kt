package com.skelterlabs.aware.example.fcm

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.skelterlabs.aware.AIQAware
import com.skelterlabs.aware.AIQAwareApp

class FcmMessagingService : FirebaseMessagingService() {

  private lateinit var aiqAware: AIQAware

  override fun onCreate() {
    super.onCreate()
    aiqAware = AIQAwareApp.getInstance(application)
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    if (aiqAware.handleAiqAwareFcmMessage(remoteMessage)) {
      showToast("AWARE message handled")
      return
    }

    // Handle client message.
    showToast("onMessageReceived")
  }

  private fun showToast(message: String) {
    Handler(Looper.getMainLooper()).post {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
  }
}
