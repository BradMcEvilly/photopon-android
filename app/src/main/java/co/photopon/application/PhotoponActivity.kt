package co.photopon.application

import android.Manifest
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import co.photopon.R
import com.uxcam.UXCam

abstract class PhotoponActivity : AppCompatActivity() {

  abstract fun isFullscreen(): Boolean

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    UXCam.startWithKey("1p78vfc6zdrv4cx")

    if (isFullscreen()) {
      window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
  }

  protected fun requestPermission(permission: Int) {
    when (permission) {
      REQUEST_CONTACTS -> ActivityCompat.requestPermissions(this,
          arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CONTACTS)
      REQUEST_LOCATION_PERMISSION -> ActivityCompat.requestPermissions(this,
          arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
      REQUEST_CAMERA_PERMISSION -> ActivityCompat.requestPermissions(this,
          arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
      REQUEST_STORAGE_PERMISSION -> ActivityCompat.requestPermissions(this,
          arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
              Manifest.permission.WRITE_EXTERNAL_STORAGE,
              Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
    }
  }

  fun showOkDialog(titleResource: Int, messageResource: Int) {
    AlertDialog.Builder(this, R.style.PhotoponTheme)
        .setTitle(titleResource)
        .setMessage(messageResource)
        .setPositiveButton(android.R.string.ok, { dialog, _ -> dialog.dismiss() })
        .show()
  }

  fun showOkDialog(titleResource: Int, messageResource: Int, dismissed: () -> Unit) {
    AlertDialog.Builder(this, R.style.PhotoponTheme)
        .setTitle(titleResource)
        .setMessage(messageResource)
        .setOnDismissListener { dismissed() }
        .setPositiveButton(android.R.string.ok, { dialog, _ -> dialog.dismiss() })
        .show()
  }

  fun displayGenericError() {
    showOkDialog(R.string.generic_error_title, R.string.generic_error_message)
  }

  fun dropOut() {
    finish()
    overridePendingTransition(R.anim.slide_down_out, R.anim.stay)
  }

  fun slideRightIn() {
    finish()
    overridePendingTransition(R.anim.slide_left_in, R.anim.stay)
  }

  fun slideLeftIn() {
    finish()
    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
  }

  fun slideUp(finish: Boolean? = false) {
    finish?.let {
      if (it) {
        finish()
      }
    }
    overridePendingTransition(R.anim.slide_up_in, R.anim.stay)
  }

  fun slideDown() {
    finish()
    overridePendingTransition(R.anim.slide_down_out, R.anim.stay)
  }

  companion object {
    const val REQUEST_LOCATION_PERMISSION = 1224
    const val REQUEST_CAMERA_PERMISSION = 2432
    const val REQUEST_STORAGE_PERMISSION = 3532
    const val REQUEST_CONTACTS = 4352
    const val REQUEST_ALL_PERMISSIONS = 10789
  }
}
