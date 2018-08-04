package co.photopon.onboarding.verify

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import kotlinx.android.synthetic.main.fragment_validate_code.*
import kotlinx.android.synthetic.main.fragment_validate_code.view.*


class VerifyCodeFragment : Fragment() {
  private var callback: OnVerifyCodeListener? = null

  interface OnVerifyCodeListener {
    fun onVerifyCodeClicked(code: String)

    fun onCancelClicked()

    fun onResendCodeClicked()
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as? OnVerifyCodeListener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

    val viewGroup = inflater.inflate(R.layout.fragment_validate_code, container, false) as ViewGroup

    viewGroup.bt_validate_code.setOnClickListener {
      callback?.onVerifyCodeClicked(et_validate_code.getText(false).toString())
    }
    viewGroup.bt_cancel.setOnClickListener { callback?.onCancelClicked() }
    viewGroup.bt_resend.setOnClickListener { callback?.onResendCodeClicked() }

    return viewGroup
  }

}