package co.photopon.main.make

import android.content.Context
import co.photopon.main.gifts.toGiftViewModels
import co.photopon.managers.GiftManager


class MakePhotoponPresenterImpl(override val view: MakePhotoponView,
                                override val context: Context,
                                override val giftManager: GiftManager) : MakePhotoponPresenter {
  override fun onCreate() {
    giftManager.getCoupons { coupons ->
      view.displayCoupons(ArrayList(coupons.toGiftViewModels()))
    }
  }

}
