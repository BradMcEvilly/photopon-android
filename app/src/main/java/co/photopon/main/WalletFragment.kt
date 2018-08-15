package co.photopon.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import kotlinx.android.synthetic.main.fragment_wallet.view.*


class WalletFragment : Fragment() {

  interface WalletListener {
    fun onHamburgerClicked()
  }

  lateinit var callback: WalletListener

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as WalletListener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    val viewGroup = inflater.inflate(R.layout.fragment_wallet, container, false) as ViewGroup

    viewGroup.ib_hamburger.setOnClickListener { callback.onHamburgerClicked() }

    return viewGroup
  }
}
