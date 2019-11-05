package com.skelterlabs.aware.example.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skelterlabs.aware.AIQAware
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

  private lateinit var aiqAware: AIQAware

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    aiqAware = (application as SimpleApp).aiqAware
    register.onClick {
      aiqAware.register()
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally { updateView() }
        .subscribe(
          { toast("AIQAware is registered") },
          { error -> toast("Error occur when registering: $error") }
        )
    }
    unregister.onClick {
      aiqAware.unregister()
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally { updateView() }
        .subscribe(
          { toast("AIQAware is unregistered") },
          { error -> toast("Error occur when unregistering: $error") }
        )
    }
    toggle_service_enabled.onClick {
      aiqAware.serviceEnabled = !aiqAware.serviceEnabled
      updateView()
    }
    updateView()
  }

  override fun onResume() {
    super.onResume()
    updateView()
  }

  private fun updateView() {
    if (aiqAware.registered) {
      is_registered.text = getString(R.string.is_registered)
      register.isEnabled = false
      unregister.isEnabled = true
    } else {
      is_registered.text = getString(R.string.is_not_registered)
      register.isEnabled = true
      unregister.isEnabled = false
    }
    if (aiqAware.serviceEnabled) {
      is_service_enabled.text = getString(R.string.is_service_enabled)
    } else {
      is_service_enabled.text = getString(R.string.is_service_disabled)
    }
  }
}
