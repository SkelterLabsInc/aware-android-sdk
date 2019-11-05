package com.skelterlabs.aware.example.fcm

import android.app.Application
import com.skelterlabs.aware.AIQAware
import java.lang.RuntimeException

class FcmApp : Application() {

  lateinit var aiqAware: AIQAware

  override fun onCreate() {
    super.onCreate()

    aiqAware = AIQAware.getInstance(this)
    if (!aiqAware.init()) {
      throw RuntimeException("API key is not set. Please set aware-client-config.json file.")
    }
  }
}
