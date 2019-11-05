package com.skelterlabs.aware.example.fcm

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.skelterlabs.aware.AIQAware
import java.lang.RuntimeException

class FcmApp : Application() {

  private val TAG = "FcmApp"

  lateinit var aiqAware: AIQAware

  override fun onCreate() {
    super.onCreate()

    aiqAware = AIQAware.getInstance(this)
    if (!aiqAware.init()) {
      throw RuntimeException("API key is not set. Please set aware-client-config.json file.")
    }

    FirebaseInstanceId.getInstance().instanceId
      .addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
          Log.w(TAG, "getInstanceId failed", task.exception)
          return@OnCompleteListener
        }

        // Get new Instance ID token
        val token = task.result?.token ?: ""
        Log.d(TAG, token)
      })
  }
}
