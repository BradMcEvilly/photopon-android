package co.photopon.chat

import android.content.Context
import co.photopon.managers.ChatManager

class ChatPresenterImpl(override val view: ChatView,
                        override val context: Context,
                        override val chatPresenter: ChatManager) : ChatPresenter {

  override fun onCreate() {
  }

  override fun sendMessage(message: String) {
  }

}
