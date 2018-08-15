package co.photopon.main.friends

import android.content.Context
import co.photopon.main.gifts.GiftViewModel
import co.photopon.managers.GiftManager
import com.parse.ParseObject

interface GiftsView {
  fun clearList()

  fun displayGifts(gifts: List<GiftViewModel>)

  fun displayNotEnoughShares(needed: Int, coupon: ParseObject)

  fun displayGiveCoupon(coupon: GiftViewModel)
}

interface GiftsPresenter {
  val view: GiftsView

  val context: Context

  val giftManager: GiftManager

  fun onRefresh()

  fun giveCoupon(coupon: GiftViewModel)

  fun getCoupon(coupon: GiftViewModel)
}
