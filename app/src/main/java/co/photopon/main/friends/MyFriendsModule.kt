package co.photopon.main.friends

import android.content.Context
import co.photopon.managers.FriendManager
import dagger.Module
import dagger.Provides

/**
 * Created by tyler on 12/14/17.
 */

@Module
class MyFriendsModule(val view: MyFriendsView) {

  @Provides
  fun provideView(): MyFriendsView = view

  @Provides
  fun providePresenter(view: MyFriendsView,
                       context: Context,
                       friendManager: FriendManager): MyFriendsPresenter =
      MyFriendsPresenterImpl(view, context, friendManager)

}
