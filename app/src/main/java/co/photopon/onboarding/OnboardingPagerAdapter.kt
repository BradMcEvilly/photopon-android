package co.photopon.onboarding

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


class OnboardingPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

  private var locationPermissionFragment: LocationPermissionFragment? = null
  private var storagePermissionFragment: StoragePermissionFragment? = null
  private var cameraPermissionFragment: CameraPermissionFragment? = null

  override fun getItem(position: Int): Fragment {
    return when (position) {
      0 -> WelcomeFragment()
      1 -> {
        locationPermissionFragment = LocationPermissionFragment()
        locationPermissionFragment!!
      }
      2 -> {
        storagePermissionFragment = StoragePermissionFragment()
        storagePermissionFragment!!
      }
      3 -> {
        cameraPermissionFragment = CameraPermissionFragment()
        cameraPermissionFragment!!
      }
      else -> RegisterFragment()
    }
  }

  override fun getCount(): Int {
    return 5
  }

  fun showLocationPermissionGranted() {
    locationPermissionFragment?.showPermissionGranted()
  }

  fun showCameraPermissionGranted() {
    cameraPermissionFragment?.showPermissionGranted()
  }

  fun showStoragePermissionGranted() {
    storagePermissionFragment?.showPermissionGranted()
  }

}
