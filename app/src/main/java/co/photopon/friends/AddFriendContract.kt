package co.photopon.friends

import android.content.Context
import co.photopon.managers.FriendManager
import com.parse.ParseUser

interface AddFriendView {
  fun clearList()

  fun displayUsers(users: List<UserViewModel>, friends: List<UserViewModel>, contacts: List<UserViewModel>)
}

interface AddFriendPresenter {
  val view: AddFriendView

  val context: Context

  val friendManager: FriendManager
  
  fun search(query: String)

  fun addFriend(user: ParseUser, completion: (success: Boolean, name: String) -> Unit)
}
