package com.skelterlabs.aware.example.fcm

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.skelterlabs.aware.AIQAwareApp

class FcmApp : MultiDexApplication() {

  companion object {
    private const val TAG = "FcmApp"
  }

  override fun onCreate() {
    super.onCreate()

    val aiqAware = AIQAwareApp.getInstance(this)
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
