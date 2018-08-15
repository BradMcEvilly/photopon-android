package co.photopon.friends

import android.content.Context
import co.photopon.managers.FriendManager
import dagger.Module
import dagger.Provides

/**
 * Created by tyler on 12/14/17.
 */

@Module
class AddFriendModule(val view: AddFriendView) {

  @Provides
  fun provideView(): AddFriendView = view

  @Provides
  fun providePresenter(view: AddFriendView,
                       context: Context,
                       friendManager: FriendManager): AddFriendPresenter =
      AddFriendPresenterImpl(view, context, friendManager)

}
