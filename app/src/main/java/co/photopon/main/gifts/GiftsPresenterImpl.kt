package co.photopon.main.friends

import android.content.Context
import co.photopon.main.gifts.GiftViewModel
import co.photopon.main.gifts.toGiftViewModel
import co.photopon.managers.GiftManager
import com.parse.ParseObject
import com.parse.ParseQuery


class GiftsPresenterImpl(override val view: GiftsView,
                         override val context: Context,
                         override val giftManager: GiftManager) : GiftsPresenter {

  private var giftsQuery: ParseQuery<ParseObject>? = null
  private var giftViewModels: ArrayList<GiftViewModel> = arrayListOf()
  private var giftsTotal = 0

  override fun onRefresh() {
    giftsQuery?.cancel()

    getGifts()
  }

  override fun getCoupon(coupon: GiftViewModel) {
    val needed = coupon.giveToGet - coupon.shares
    if (needed > 0) {
      view.displayNotEnoughShares(needed, coupon.parseObject)
    }
  }

  override fun giveCoupon(coupon: GiftViewModel) {
    view.displayGiveCoupon(coupon)
  }

  private fun getGifts() {
    giftsQuery?.cancel()
    giftsTotal = 0
    giftViewModels = arrayListOf()

    giftsQuery = giftManager.getCoupons({ coupons ->
      giftsTotal = coupons.count()

      coupons.forEach { coupon ->
        giftManager.getCouponShares(coupon, { shares ->
          giftViewModels.add(coupon.toGiftViewModel(shares))
          checkAndDisplayGifts()
        })
      }
    })
  }

  private fun displayGifts(friends: List<GiftViewModel>) {
    view.clearList()
    view.displayGifts(friends)
  }

  private fun checkAndDisplayGifts() {
    if (giftViewModels.count() == giftsTotal) {
      displayGifts(giftViewModels.sortedWith(compareBy({ it.expires })).toList())
    }
  }
}
