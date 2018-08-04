package co.photopon.onboarding

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import co.photopon.R
import co.photopon.application.PhotoponActivity
import co.photopon.main.MainActivity
import co.photopon.onboarding.verify.VerifyNumberActivity
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : PhotoponActivity(), LocationPermissionFragment.OnPermissionRequested,
    CameraPermissionFragment.OnPermissionRequested, StoragePermissionFragment.OnPermissionRequested,
    RegisterFragment.OnRegisterListener {

  lateinit var pagerAdapter: OnboardingPagerAdapter

  private var permissions = hashMapOf(
      REQUEST_LOCATION_PERMISSION to false,
      REQUEST_CAMERA_PERMISSION to false,
      REQUEST_STORAGE_PERMISSION to false
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_onboarding)

    pagerAdapter = OnboardingPagerAdapter(supportFragmentManager)

    view_pager.setAdapter(pagerAdapter)
    view_pager.invalidateBullets()
  }

  override fun isFullscreen(): Boolean {
    return false
  }

  override fun onLocationPermissionRequested() {
    requestPermission(REQUEST_LOCATION_PERMISSION)
  }

  override fun onCameraPermissionRequested() {
    requestPermission(REQUEST_CAMERA_PERMISSION)
  }

  override fun onStoragePermissionRequested() {
    requestPermission(REQUEST_STORAGE_PERMISSION)
  }

  override fun onRequestPermissionsResult(requestCode: Int,
                                          permissions: Array<String>,
                                          grantResults: IntArray) {
    val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

    if (granted) {
      this.permissions[requestCode] = true

      when (requestCode) {
        REQUEST_LOCATION_PERMISSION -> pagerAdapter.showLocationPermissionGranted()
        REQUEST_CAMERA_PERMISSION -> pagerAdapter.showCameraPermissionGranted()
        REQUEST_STORAGE_PERMISSION -> pagerAdapter.showStoragePermissionGranted()
        REQUEST_ALL_PERMISSIONS -> nextActivity()
      }
    }
  }

  override fun onRegisterClicked() {
    if (permissions.containsValue(false)) {
      showOkDialog(R.string.onboarding_permissions, R.string.onboarding_all_permissions) {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_ALL_PERMISSIONS)
      }
    } else {
      nextActivity()
    }
  }

  override fun onTryItOutClicked() {
    startActivity(MainActivity.getIntent(this))
    slideRightIn()
  }

  override fun onCameraViewCreated() {
    val cameraPermission = ContextCompat.checkSelfPermission(this,
        Manifest.permission.CAMERA)

    if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
      permissions[REQUEST_CAMERA_PERMISSION] = true

      displayCameraPermissionGranted()
    }
  }

  override fun onLocationViewCreated() {
    val locationPermission = ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)

    if (locationPermission == PackageManager.PERMISSION_GRANTED) {
      permissions[REQUEST_LOCATION_PERMISSION] = true

      displayLocationPermissionGranted()
    }
  }

  override fun onStorageViewCreated() {
    val storagePermission = ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_EXTERNAL_STORAGE)

    if (storagePermission == PackageManager.PERMISSION_GRANTED) {
      permissions[REQUEST_STORAGE_PERMISSION] = true

      displayStoragePermissionGranted()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
      REQUEST_VERIFY_NUMBER -> {
        startActivity(MainActivity.getIntent(this))
        slideUp(true)
      }
    }
  }

  private fun nextActivity() {
    startActivityForResult(VerifyNumberActivity.getIntent(this), REQUEST_VERIFY_NUMBER)
    slideUp(false)
  }

  private fun displayLocationPermissionGranted() {
    pagerAdapter.showLocationPermissionGranted()
  }

  private fun displayCameraPermissionGranted() {
    pagerAdapter.showCameraPermissionGranted()
  }

  private fun displayStoragePermissionGranted() {
    pagerAdapter.showStoragePermissionGranted()
  }

  companion object {

    const val REQUEST_VERIFY_NUMBER = 9092

    fun getIntent(context: Context): Intent {
      return Intent(context, OnboardingActivity::class.java)
    }
  }
}
