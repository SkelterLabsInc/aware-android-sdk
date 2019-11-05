package com.skelterlabs.aware.example.fcm

import android.os.Handler
import android.os.Looper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.skelterlabs.aware.AIQAware
import org.jetbrains.anko.toast

class FcmMessagingService : FirebaseMessagingService() {

  private lateinit var aiqAware: AIQAware

  override fun onCreate() {
    super.onCreate()
    aiqAware = AIQAware.getInstance(application)
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    if (aiqAware.handleAiqAwareFcmMessage(remoteMessage)) {
      toast("AWARE message handled")
      return
    }

    // Handle client message.
    toast("onMessageReceived")
  }

  private fun toast(message: String) {
    Handler(Looper.getMainLooper()).post {
      application.toast(message)
    }
  }
}
