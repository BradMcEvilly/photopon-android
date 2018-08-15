package co.photopon.main.draw

import android.content.Context
import co.photopon.main.make.MakePhotoponPresenter
import co.photopon.main.make.MakePhotoponPresenterImpl
import co.photopon.main.make.MakePhotoponView
import co.photopon.managers.GiftManager
import dagger.Module
import dagger.Provides

/**
 * Created by tyler on 12/14/17.
 */

@Module
class DrawCouponModule(val view: DrawCouponView) {

  @Provides
  fun provideView(): DrawCouponView = view

  @Provides
  fun providePresenter(view: DrawCouponView,
                       context: Context,
                       giftManager: GiftManager): DrawCouponPresenter =
      DrawCouponPresenterImpl(view, context, giftManager)

}
