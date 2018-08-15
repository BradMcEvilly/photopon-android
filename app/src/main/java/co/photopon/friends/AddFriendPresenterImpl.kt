package co.photopon.friends

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.support.v4.content.CursorLoader
import co.photopon.managers.FriendManager
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser


class AddFriendPresenterImpl(override val view: AddFriendView,
                             override val context: Context,
                             override val friendManager: FriendManager) : AddFriendPresenter {

  private var users: List<UserViewModel>? = null
  private var friends: List<UserViewModel>? = null
  private var contacts: List<UserViewModel>? = null

  private var userQuery: ParseQuery<ParseUser>? = null
  private var friendsQuery: ParseQuery<ParseObject>? = null

  override fun search(query: String) {
    users = null
    friends = null
    contacts = null
    userQuery?.cancel()
    friendsQuery?.cancel()

    userQuery = friendManager.queryUsers(query, {
      users = it
      checkAndDisplayUsers()
    }, {

    })

    friendsQuery = friendManager.getFriends({ objects ->
      friends = objects.filter { it["user2"] != null }
          .toUserViewModelsFromObject(UserViewModelType.USER_FRIEND)
      checkAndDisplayUsers()
    }, {
    })

    val contactsPermission = ContextCompat.checkSelfPermission(context,
        Manifest.permission.READ_CONTACTS)
    if (contactsPermission == PackageManager.PERMISSION_GRANTED) {
      queryContacts(query)
    }
  }

  override fun addFriend(user: ParseUser, completion: (success: Boolean, user: String) -> Unit) {
    friendManager.addFriend(user) { success ->
      completion(success, user.username)
    }
  }

  private fun queryContacts(query: String) {
    var results = arrayListOf<UserViewModel>()

    val queryUri = ContactsContract.Contacts.CONTENT_URI
    val projection = arrayOf(ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.PHOTO_URI,
        ContactsContract.Contacts.HAS_PHONE_NUMBER)
    val selection = ContactsContract.Contacts.DISPLAY_NAME + " IS NOT NULL"
    val cursorLoader = CursorLoader(
        context,
        queryUri,
        projection,
        selection,
        null, null)
    val cursor = cursorLoader.loadInBackground()

    cursor?.let {
      val columnId = it.getColumnIndex(ContactsContract.Contacts._ID)
      val columnName = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
      val columnPhotoUri = it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
      val columnHasPhone = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)

      while (it.moveToNext()) {
        val id = it.getInt(columnId)
        val displayName = it.getString(columnName)
        val photoUri = it.getString(columnPhotoUri)
        val hasPhone = it.getString(columnHasPhone)
        var phone = ""

        if (!hasPhone.endsWith("0")) {
          val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
          phone = getPhoneNumber(id)
        }

        if (Regex("$query.*", RegexOption.IGNORE_CASE).matches(displayName)) {
          results.add(UserViewModel(UserViewModelType.USER_CONTACT, "$id", displayName, phone, null,
              photoUri))
        }
      }
    }

    contacts = results
    cursor?.close()
  }

  private fun getPhoneNumber(id: String): String {
    var number = ""
    val phones = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null, ContactsContract.CommonDataKinds.Phone._ID + " = " + id, null, null)

    if (phones.count > 0) {
      while (phones.moveToNext()) {
        number = phones.getString(
            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
      }
    }

    phones.close()
    return number
  }

  private fun checkAndDisplayUsers() {
    if (users != null && friends != null) {
      view.displayUsers(users!!.filter { !friends!!.map { it.id }.contains(it.id) },
          friends ?: listOf(), contacts ?: listOf())
    }
  }

}
