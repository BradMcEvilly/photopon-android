package co.photopon.main.make

import android.content.Context
import co.photopon.managers.GiftManager
import dagger.Module
import dagger.Provides

/**
 * Created by tyler on 12/14/17.
 */

@Module
class MakePhotoponModule(val view: MakePhotoponView) {

  @Provides
  fun provideView(): MakePhotoponView = view

  @Provides
  fun providePresenter(view: MakePhotoponView,
                       context: Context,
                       giftManager: GiftManager): MakePhotoponPresenter =
      MakePhotoponPresenterImpl(view, context, giftManager)

}
