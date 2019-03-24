package co.photopon.friends

import android.content.Context
import co.photopon.managers.FriendManager
import co.photopon.managers.GiftManager
import co.photopon.managers.NotificationManager
import com.parse.ParseFile
import com.parse.ParseObject

interface ShareGiftView {
  fun clearList()

  fun displayFriends(friends: List<UserViewModel>)

  fun onShareClicked()

  fun displayShared()
}

interface ShareGiftPresenter {
  val view: ShareGiftView

  val context: Context

  val giftManager: GiftManager

  val friendManager: FriendManager

  val notificationManager: NotificationManager

  fun onRefresh()

  fun share(friends: List<UserViewModel>, coupon: ParseObject, photo: ParseFile, drawing: ParseFile,
            completion: (success: Boolean) -> Unit)
}
