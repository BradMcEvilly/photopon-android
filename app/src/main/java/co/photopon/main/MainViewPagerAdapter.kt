package co.photopon.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import co.photopon.main.friends.MyFriendsFragment
import co.photopon.main.gifts.GiftViewModel
import co.photopon.main.gifts.GiftsFragment
import co.photopon.main.make.MakePhotoponFragment
import co.photopon.main.notifications.NotificationsFragment

class MainViewPagerAdapter(fm: FragmentManager?,
                           private val preselectedCoupon: GiftViewModel? = null) : FragmentPagerAdapter(fm) {

  override fun getItem(position: Int): Fragment {
    val index = position % 5

    var frag = fragments[index]

    if (frag == null) {
      frag = when (index) {
        0 -> {
          val frag = MakePhotoponFragment()
          val args = Bundle()
          preselectedCoupon?.let { args.putParcelable(ARGUMENT_PRESELECTED_COUPON, it) }
          frag.arguments = args
          frag
        }
        1 -> NotificationsFragment()
        2 -> MyFriendsFragment()
        3 -> GiftsFragment()
        else -> WalletFragment()
      }
    }

    return frag
  }

  override fun getCount(): Int {
    return Integer.MAX_VALUE
  }

  companion object {
    const val ARGUMENT_PRESELECTED_COUPON = "argumentPreselectedCoupon"
    val fragments = hashMapOf<Int, Fragment>()
  }
}
