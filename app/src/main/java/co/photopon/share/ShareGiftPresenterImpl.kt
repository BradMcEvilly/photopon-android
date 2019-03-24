package co.photopon.friends

import android.content.Context
import co.photopon.managers.FriendManager
import co.photopon.managers.GiftManager
import co.photopon.managers.NotificationManager
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser


class ShareGiftPresenterImpl(override val view: ShareGiftView,
                             override val context: Context,
                             override val friendManager: FriendManager,
                             override val notificationManager: NotificationManager,
                             override val giftManager: GiftManager) : ShareGiftPresenter {

  private var friendsQuery: ParseQuery<ParseObject>? = null

  override fun onRefresh() {
    getFriends()
  }

  override fun share(friends: List<UserViewModel>, coupon: ParseObject, photo: ParseFile,
                     drawing: ParseFile, completion: (success: Boolean) -> Unit) {
    giftManager.sendPhotopons(coupon, drawing, photo, friends.map { it.parseUser ?: ParseUser() }, { success ->
      completion(success)
    })
  }

  private fun getFriends() {
    friendsQuery = friendManager.getFriends({ friends ->
      view.displayFriends(friends.filter { it["user2"] != null }
          .toUserViewModelsFromObject()
          .sortedWith(compareBy({ it.fullName.toLowerCase() })))
    }, {

    })
  }

}
