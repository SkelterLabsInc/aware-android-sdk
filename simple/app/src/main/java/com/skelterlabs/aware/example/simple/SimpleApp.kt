package com.skelterlabs.aware.example.simple

import androidx.multidex.MultiDexApplication
import com.skelterlabs.aware.AIQAwareApp

class SimpleApp : MultiDexApplication() {

  override fun onCreate() {
    super.onCreate()

    val aiqAware = AIQAwareApp.getInstance(this)
    if (!aiqAware.init()) {
      throw RuntimeException("API key is not set. Please set aware-client-config.json file.")
    }
  }
}
