package com.skelterlabs.aware.example.simple

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.skelterlabs.aware.AIQAware
import com.skelterlabs.aware.AIQAwareApp
import com.skelterlabs.aware.AIQAwareException
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

  private lateinit var aiqAware: AIQAware

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    aiqAware = AIQAwareApp.getInstance(application)

    register.setOnClickListener(this)
    unregister.setOnClickListener(this)
    toggle_service_enabled.setOnClickListener(this)

    requestPermission()
  }

  override fun onResume() {
    super.onResume()
    updateView()
  }

  override fun onClick(view: View) {
    when (view) {
      register -> {
        aiqAware.register(null, null, object : AIQAware.Callback<String> {
          override fun onSuccess(result: String) {
            showToast("AIQAware is registered")
            updateView()
          }

          override fun onError(e: AIQAwareException) {
            showToast("Error occurred when registering: $e")
            updateView()
          }
        })
      }

      unregister -> {
        aiqAware.unregister(object : AIQAware.Callback<Unit> {
          override fun onSuccess(result: Unit) {
            showToast("AIQAware is unregistered")
            updateView()
          }

          override fun onError(e: AIQAwareException) {
            showToast("Error occurred when unregistering: $e")
            updateView()
          }
        })
      }

      toggle_service_enabled -> {
        aiqAware.setEnableService(!aiqAware.isServiceEnabled())
        updateView()
      }
    }
  }

  private fun requestPermission() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      return
    }

    val permission = Manifest.permission.ACCESS_FINE_LOCATION
    if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
      requestPermissions(arrayOf(permission), 1)
    }
  }

  private fun updateView() {
    if (aiqAware.isRegistered()) {
      is_registered.text = getString(R.string.is_registered)
      register.isEnabled = false
      unregister.isEnabled = true
    } else {
      is_registered.text = getString(R.string.is_not_registered)
      register.isEnabled = true
      unregister.isEnabled = false
    }

    if (aiqAware.isServiceEnabled()) {
      is_service_enabled.text = getString(R.string.is_service_enabled)
    } else {
      is_service_enabled.text = getString(R.string.is_service_disabled)
    }
  }

  private fun showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }
}
