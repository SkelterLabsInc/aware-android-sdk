package com.skelterlabs.aware.example.simple

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.skelterlabs.aware.AIQAware
import com.skelterlabs.aware.AIQAwareApp
import com.skelterlabs.aware.AIQAwareException
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity(), View.OnClickListener {

  private lateinit var aiqAware: AIQAware

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    aiqAware = AIQAwareApp.getInstance(application)

    register_button.setOnClickListener(this)
    unregister_button.setOnClickListener(this)
    toggle_service_button.setOnClickListener(this)
    profile_button.setOnClickListener(this)

    requestPermission()
  }

  override fun onResume() {
    super.onResume()
    updateView()
  }

  override fun onClick(view: View) {
    when (view) {
      register_button -> {
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

      unregister_button -> {
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

      toggle_service_button -> {
        aiqAware.setEnableService(!aiqAware.isServiceEnabled())
        updateView()
      }

      profile_button -> {
        aiqAware.getProfile(object : AIQAware.Callback<String> {
          override fun onSuccess(result: String) {
            try {
              val json = JSONObject(result)
              profile_label.text = json.toString(2)
            } catch (e: JSONException) {
              profile_label.text = e.toString()
            }
          }

          override fun onError(e: AIQAwareException) {
            profile_label.text = e.toString()
          }
        })
      }
    }
  }

  private fun requestPermission() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      return
    }

    val permissions = listOf(
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_WIFI_STATE,
      Manifest.permission.BLUETOOTH,
      Manifest.permission.CHANGE_WIFI_STATE
    )
    requestPermissions(permissions.toTypedArray(), 1)
  }

  private fun updateView() {
    if (aiqAware.isRegistered()) {
      registered_label.text = getString(R.string.is_registered)
      register_button.isEnabled = false
      unregister_button.isEnabled = true
    } else {
      registered_label.text = getString(R.string.is_not_registered)
      register_button.isEnabled = true
      unregister_button.isEnabled = false
    }

    if (aiqAware.isServiceEnabled()) {
      service_enabled_label.text = getString(R.string.is_service_enabled)
    } else {
      service_enabled_label.text = getString(R.string.is_service_disabled)
    }

    profile_label.text = ""
  }

  private fun showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }
}
