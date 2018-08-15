package co.photopon.chat

import android.content.Context
import co.photopon.managers.ChatManager

interface ChatView {
  fun showIncomingMessages(messages: MutableList<ChatMessageViewModel>)

  fun showIncomingMessage(message: ChatMessageViewModel)
}

interface ChatPresenter {
  val view: ChatView

  val context: Context

  val chatPresenter: ChatManager

  fun onCreate()

  fun sendMessage(message: String)
}
