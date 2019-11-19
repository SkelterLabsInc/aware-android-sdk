package com.skelterlabs.aware.example.simple

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.skelterlabs.aware.AIQAware
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

  companion object {
    private const val PERMISSION_REQUEST_CODE = 1234
  }

  private lateinit var aiqAware: AIQAware
  private val disposableBag = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    aiqAware = (application as SimpleApp).aiqAware

    register.onClick {
      val registration = aiqAware.register()
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally { updateView() }
        .subscribe(
          { toast("AIQAware is registered") },
          { error -> toast("Error occur when registering: $error") }
        )

      disposableBag.add(registration)
    }
    unregister.onClick {
      val unregistration = aiqAware.unregister()
        .observeOn(AndroidSchedulers.mainThread())
        .doFinally { updateView() }
        .subscribe(
          { toast("AIQAware is unregistered") },
          { error -> toast("Error occur when unregistering: $error") }
        )

      disposableBag.add(unregistration)
    }
    toggle_service_enabled.onClick {
      aiqAware.serviceEnabled = !aiqAware.serviceEnabled
      updateView()
    }

    requestPermissions()
    updateView()
  }

  override fun onResume() {
    super.onResume()
    updateView()
  }

  override fun onDestroy() {
    super.onDestroy()

    disposableBag.clear()
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

  private fun requestPermissions() {

    val permissions = listOf(
      Manifest.permission.BLUETOOTH,
      Manifest.permission.READ_CALENDAR,
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.CHANGE_WIFI_STATE,
      Manifest.permission.ACCESS_WIFI_STATE
    )
      .toTypedArray()

    ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
  }
}
