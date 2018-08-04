package co.photopon.main.friends

import android.content.Context
import co.photopon.R
import co.photopon.friends.UserViewModel
import co.photopon.friends.UserViewModelType
import co.photopon.managers.FriendManager
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser


class MyFriendsPresenterImpl(override val view: MyFriendsView,
                             override val context: Context,
                             override val friendManager: FriendManager) : MyFriendsPresenter {

  private var friendsQuery: ParseQuery<ParseObject>? = null
  private var friends: ArrayList<UserViewModel>? = null

  private var totalFriends = 0

  override fun onRefresh() {
    getFriends()

    friendsQuery?.cancel()
    friends = arrayListOf()
  }

  override fun chatClicked(friend: UserViewModel) {
    view.displayChat(friend)
  }

  override fun friendClicked(friend: UserViewModel) {
    view.aboutFriend(friend)
  }

  private fun getFriends() {
    friends = arrayListOf()
    totalFriends = 0

    friendsQuery = friendManager.getFriends({ friends ->
      totalFriends = friends.count()

      friends.forEach { friend ->
        friendManager.getUserShares(friend, { shared, used ->
          val user2 = friend["user2"]
          var friended = UserViewModelType.USER_FRIEND
          var extended = context.getString(R.string.my_friends_shared_used, shared, used)
          var name = (user2 as? ParseUser)?.username ?: friend["name"] as? String ?: ""
          if (user2 == null) {
            friended = UserViewModelType.USER_PENDING
            extended = friend["phone"] as? String
          }
          val viewModel = UserViewModel(friended, friend.objectId, name, extended)

          this.friends?.add(viewModel)

          checkAndDisplayFriends()
        })
      }
    }, {
    })
  }

  private fun checkAndDisplayFriends() {
    friends?.let { friends ->
      if (totalFriends == friends.count())
        displayFriends(friends.sortedWith(compareBy({ it.type },{ it.fullName.toLowerCase() })))
    }
  }

  private fun displayFriends(friends: List<UserViewModel>) {
    view.clearList()
    view.displayFriends(friends)
  }

}
