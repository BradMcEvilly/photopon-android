package co.photopon.onboarding.verify

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import kotlinx.android.synthetic.main.fragment_validate_number.*
import kotlinx.android.synthetic.main.fragment_validate_number.view.*


class VerifyPhoneFragment : Fragment() {
  private var callback: OnVerifyNumberListener? = null

  interface OnVerifyNumberListener {
    fun onVerifyNumberClicked(number: String)

    fun onLaterClicked()
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as? OnVerifyNumberListener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val viewGroup = inflater.inflate(R.layout.fragment_validate_number, container, false) as ViewGroup

    viewGroup.bt_validate_code.setOnClickListener { verifyClicked(et_validate_code.text.toString()) }
    viewGroup.bt_later.setOnClickListener { laterClicked() }

    return viewGroup
  }

  private fun verifyClicked(number: String) {
    callback?.onVerifyNumberClicked(number)
  }

  private fun laterClicked() {
    callback?.onLaterClicked()
  }
}
