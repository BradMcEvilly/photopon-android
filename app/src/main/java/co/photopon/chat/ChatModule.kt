package co.photopon.chat

import android.content.Context
import co.photopon.friends.AddFriendPresenter
import co.photopon.friends.AddFriendPresenterImpl
import co.photopon.friends.AddFriendView
import co.photopon.managers.ChatManager
import co.photopon.managers.FriendManager
import dagger.Module
import dagger.Provides

/**
 * Created by tyler on 12/14/17.
 */
@Module
class ChatModule(val view: ChatView) {

  @Provides
  fun provideView(): ChatView = view

  @Provides
  fun providePresenter(view: ChatView,
                       context: Context,
                       chatManager: ChatManager): ChatPresenter =
      ChatPresenterImpl(view, context, chatManager)

}
