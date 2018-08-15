package co.photopon.friends

import android.content.Context
import co.photopon.managers.FriendManager
import co.photopon.managers.GiftManager
import dagger.Module
import dagger.Provides

/**
 * Created by tyler on 12/14/17.
 */

@Module
class ShareGiftModule(val view: ShareGiftView) {

  @Provides
  fun provideView(): ShareGiftView = view

  @Provides
  fun providePresenter(view: ShareGiftView,
                       context: Context,
                       friendManager: FriendManager,
                       giftManager: GiftManager): ShareGiftPresenter =
      ShareGiftPresenterImpl(view, context, friendManager, giftManager)

}
