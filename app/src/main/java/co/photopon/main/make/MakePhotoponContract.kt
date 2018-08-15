package co.photopon.main.make

import android.content.Context
import co.photopon.main.gifts.GiftViewModel
import co.photopon.managers.GiftManager

interface MakePhotoponView {
  fun displayCoupons(friends: ArrayList<GiftViewModel>)
}

interface MakePhotoponPresenter {
  val view: MakePhotoponView

  val context: Context

  val giftManager: GiftManager

  fun onCreate()
}
