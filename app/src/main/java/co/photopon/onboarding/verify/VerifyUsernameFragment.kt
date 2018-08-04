package co.photopon.onboarding.verify

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import kotlinx.android.synthetic.main.fragment_validate_username.*
import kotlinx.android.synthetic.main.fragment_validate_username.view.*


class VerifyUsernameFragment : Fragment() {

  private var callback: OnGetStartedListener? = null

  interface OnGetStartedListener {
    fun onGetStartedClicked(userName: String)

    fun onLaterClicked()
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as? OnGetStartedListener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val viewGroup = inflater.inflate(R.layout.fragment_validate_username, container, false) as ViewGroup

    viewGroup.bt_register.setOnClickListener { registerClicked(et_username.text.toString()) }
    viewGroup.bt_later.setOnClickListener { callback?.onLaterClicked() }

    return viewGroup
  }

  private fun registerClicked(number: String) {
    callback?.onGetStartedClicked(number)
  }
}