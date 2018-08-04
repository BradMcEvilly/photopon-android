package co.photopon.managers

import co.photopon.friends.UserViewModel
import co.photopon.friends.UserViewModelType
import co.photopon.friends.toUserViewModels
import com.parse.ParseACL
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser


class FriendManager {

  fun addFriend(user: ParseUser, completion: (success: Boolean) -> Unit) {
    val me = ParseUser.getCurrentUser()
    val request = ParseObject("Friends")

    val acl = ParseACL()
    acl.publicReadAccess = true
    acl.setWriteAccess(me, true)
    request.acl = acl

    request.put("user1", me)
    request.put("user2", user)

    request.saveInBackground {
      completion(it == null)
    }
  }

  fun queryUsers(userName: String, users: (users: List<UserViewModel>) -> Unit,
                 error: () -> Unit): ParseQuery<ParseUser> {
    val searchRegex = Regex("$userName.*", RegexOption.IGNORE_CASE).toString()

    val query = ParseUser.getQuery()
    query.whereMatches("username", searchRegex)
    query.limit = 10

    query.findInBackground { objects, e ->
      when {
        e != null -> {
          error()
        }
        else -> {
          users(objects.toUserViewModels(UserViewModelType.USER_PHOTOPON))
        }
      }
    }

    return query
  }

  fun getFriends(friends: (friends: List<ParseObject>) -> Unit,
                 error: () -> Unit): ParseQuery<ParseObject> {
    val query = ParseQuery<ParseObject>("Friends")
    query.include("user2")
    query.include("name")
    query.include("phone")
    query.whereEqualTo("user1", ParseUser.getCurrentUser())

    query.findInBackground { objects, e ->
      when {
        e != null -> error()
        else -> friends(objects)
      }
    }

    return query
  }

  fun getUserShares(user: ParseObject, data: (shared: Int, used: Int) -> Unit): ParseQuery<ParseObject> {
    val query = ParseQuery<ParseObject>("PerUserShare")
    query.whereEqualTo("user", ParseUser.getCurrentUser())
    query.whereEqualTo("friend", user)

    query.countInBackground { shares, _ ->
      val query = ParseQuery<ParseObject>("Redeemed")
      query.whereEqualTo("from", ParseUser.getCurrentUser())
      query.whereEqualTo("to", user)

      query.countInBackground { uses, _ ->
        data(shares, uses)
      }
    }

    return query
  }

}
