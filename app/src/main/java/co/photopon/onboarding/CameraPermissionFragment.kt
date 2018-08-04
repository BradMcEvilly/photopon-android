package co.photopon.onboarding

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import kotlinx.android.synthetic.main.fragment_onboarding_camera.*
import kotlinx.android.synthetic.main.fragment_onboarding_camera.view.*


class CameraPermissionFragment : Fragment() {

  private var callback: OnPermissionRequested? = null

  interface OnPermissionRequested {
    fun onCameraPermissionRequested()

    fun onCameraViewCreated()
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as? OnPermissionRequested
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

    val viewGroup = inflater.inflate(R.layout.fragment_onboarding_camera, container,
        false) as ViewGroup

    viewGroup.bt_enable_camera.setOnClickListener { callback?.onCameraPermissionRequested() }

    return viewGroup
  }

  override fun onResume() {
    super.onResume()
    callback?.onCameraViewCreated()
  }

  fun showPermissionGranted() {
    if (bt_enable_camera != null) {
      bt_enable_camera.isEnabled = false
      bt_enable_camera.text = getString(R.string.onboarding_enabled)
    }
  }

}
