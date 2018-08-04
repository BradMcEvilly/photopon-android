package co.photopon.onboarding

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import kotlinx.android.synthetic.main.fragment_onboarding_location.*
import kotlinx.android.synthetic.main.fragment_onboarding_location.view.*


class LocationPermissionFragment : Fragment() {

  private var callback: OnPermissionRequested? = null

  interface OnPermissionRequested {
    fun onLocationPermissionRequested()

    fun onLocationViewCreated()
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as? OnPermissionRequested
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

    val viewGroup = inflater.inflate(R.layout.fragment_onboarding_location, container,
        false) as ViewGroup

    viewGroup.bt_enable_location.setOnClickListener { callback?.onLocationPermissionRequested() }

    return viewGroup
  }

  override fun onResume() {
    super.onResume()
    callback?.onLocationViewCreated()
  }

  fun showPermissionGranted() {
    bt_enable_location.isEnabled = false
    bt_enable_location.text = getString(R.string.onboarding_enabled)
  }

}
