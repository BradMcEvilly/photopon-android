package co.photopon.onboarding

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import kotlinx.android.synthetic.main.fragment_onboarding_storage.*
import kotlinx.android.synthetic.main.fragment_onboarding_storage.view.*


class StoragePermissionFragment : Fragment() {

  private var callback: OnPermissionRequested? = null

  interface OnPermissionRequested {
    fun onStoragePermissionRequested()

    fun onStorageViewCreated()
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as? OnPermissionRequested
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

    val viewGroup = inflater.inflate(R.layout.fragment_onboarding_storage, container,
        false) as ViewGroup

    viewGroup.bt_enable_storage.setOnClickListener { callback?.onStoragePermissionRequested() }

    return viewGroup
  }

  override fun onResume() {
    super.onResume()
    callback?.onStorageViewCreated()
  }

  fun showPermissionGranted() {
    if (bt_enable_storage != null) {
      bt_enable_storage.isEnabled = false
      bt_enable_storage.text = getString(R.string.onboarding_enabled)
    }
  }

}
