package com.skelterlabs.aware.example.profile

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.skelterlabs.aware.AIQAware
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  companion object {
    const val TAG = "MainActivity"
    const val PERMISSION_REQUEST_CODE = 1234
  }

  private lateinit var aiqAware: AIQAware
  private val disposeBag = CompositeDisposable()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    profile_button.setOnClickListener {
      requestPermissions()
      showProfile()
    }

    aiqAware = (application as ProfileApp).aiqAware
  }

  override fun onResume() {
    super.onResume()

    if (!aiqAware.registered) {
      aiqAware.register()
        .subscribe({}, { error ->
          Log.e(TAG, "Error occurred while registration: $error")
        })
        .apply { disposeBag.add(this) }
    }
  }

  override fun onStop() {
    super.onStop()

    disposeBag.clear()
  }

  private fun showProfile() {
    aiqAware.getProfile()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ profile ->
        profile_text_view.text = profile
      }, { error ->
        Log.e(TAG, "Error occurred while getting profile: $error")
      })
      .apply { disposeBag.add(this) }
  }

  private fun requestPermissions() {
    val permissions = listOf(
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_WIFI_STATE,
      Manifest.permission.BLUETOOTH,
      Manifest.permission.CHANGE_WIFI_STATE
    )
      .toTypedArray()

    ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
  }
}
