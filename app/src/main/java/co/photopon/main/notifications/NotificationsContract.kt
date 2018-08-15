package co.photopon.main.friends

import android.content.Context
import co.photopon.friends.UserViewModel
import co.photopon.main.gifts.NotificationViewModel
import co.photopon.managers.NotificationManager

interface NotificationsView {
  fun clearList()

  fun displayNotifications(notifications: List<NotificationViewModel>)

  fun displayChat(friend: UserViewModel)
}

interface NotificationsPresenter {
  val view: NotificationsView

  val context: Context

  val notificationManager: NotificationManager

  fun onRefresh()

  fun onChatClicked(friend: UserViewModel)
}
