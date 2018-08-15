package co.photopon.main.friends

import android.content.Context
import co.photopon.friends.UserViewModel
import co.photopon.managers.FriendManager

interface MyFriendsView {
  fun clearList()

  fun displayFriends(friends: List<UserViewModel>)

  fun aboutFriend(friend: UserViewModel)

  fun displayChat(friend: UserViewModel)
}

interface MyFriendsPresenter {
  val view: MyFriendsView

  val context: Context

  val friendManager: FriendManager

  fun onRefresh()

  fun friendClicked(friend: UserViewModel)

  fun chatClicked(friend: UserViewModel)
}
