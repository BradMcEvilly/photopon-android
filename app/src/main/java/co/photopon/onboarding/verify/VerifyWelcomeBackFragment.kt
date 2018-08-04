package co.photopon.onboarding.verify

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import kotlinx.android.synthetic.main.fragment_validate_welcome_back.view.*


class VerifyWelcomeBackFragment : Fragment() {

  private var callback: OnGetStartedListener? = null

  interface OnGetStartedListener {
    fun onGetStartedClicked()
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as? OnGetStartedListener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val viewGroup = inflater.inflate(R.layout.fragment_validate_welcome_back, container, false) as ViewGroup

    viewGroup.bt_get_started.setOnClickListener { callback?.onGetStartedClicked() }

    return viewGroup
  }

  fun setWelcomeBackName(name: String) {
//    tv_username.text = getString(R.string.verify_welcome_back_name, name)
  }

}