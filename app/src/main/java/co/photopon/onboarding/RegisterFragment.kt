package co.photopon.onboarding

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import kotlinx.android.synthetic.main.fragment_onboarding_enjoy.view.*


class RegisterFragment : Fragment() {
  private var callback: OnRegisterListener? = null

  interface OnRegisterListener {
    fun onRegisterClicked()

    fun onTryItOutClicked()
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as? OnRegisterListener
  }


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val viewGroup = inflater.inflate(R.layout.fragment_onboarding_enjoy, container, false) as ViewGroup

    viewGroup.bt_register.setOnClickListener { callback?.onRegisterClicked() }
    viewGroup.bt_try.setOnClickListener { callback?.onTryItOutClicked() }

    return viewGroup
  }

}
