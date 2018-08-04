package co.photopon.main.friends

import android.content.Context
import co.photopon.friends.UserViewModel
import co.photopon.main.notifications.toNotificationViewModels
import co.photopon.managers.NotificationManager


class NotificationsPresenterImpl(override val view: NotificationsView,
                                 override val context: Context,
                                 override val notificationManager: NotificationManager) :
    NotificationsPresenter {

  override fun onRefresh() {
    notificationManager.getNotifications { notifications ->
      view.displayNotifications(notifications.toNotificationViewModels())
    }
  }

  override fun onChatClicked(friend: UserViewModel) {
    view.displayChat(friend)
  }

}
