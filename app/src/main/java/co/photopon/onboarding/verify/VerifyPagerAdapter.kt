package co.photopon.onboarding.verify

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


class VerifyPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

  override fun getItem(position: Int): Fragment {
    return when (position) {
      PAGE_VERIFY_PHONE -> VerifyPhoneFragment()
      PAGE_SENDING_CODE -> VerifySendingFragment()
      PAGE_VERIFY_CODE -> VerifyCodeFragment()
      PAGE_WELCOME_BACK -> VerifyWelcomeBackFragment()
      else -> VerifyUsernameFragment()
    }
  }

  override fun getCount(): Int {
    return 5
  }

  fun setWelcomeBackName(name: String) {
    val frag = getItem(PAGE_WELCOME_BACK) as VerifyWelcomeBackFragment
    frag.setWelcomeBackName(name)
  }

  companion object {
    var PAGE_VERIFY_PHONE = 0
    var PAGE_SENDING_CODE = 1
    var PAGE_VERIFY_CODE = 2
    var PAGE_WELCOME_BACK = 3
    var PAGE_CHOOSE_USERNAME = 4
  }
}
