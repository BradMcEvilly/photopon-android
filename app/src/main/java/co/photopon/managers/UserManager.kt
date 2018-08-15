package co.photopon.managers

import com.parse.*
import java.util.*


class UserManager {

  fun getCurrentUser(): ParseUser {
    return ParseUser.getCurrentUser()
  }

  fun registerUser(userName: String, phone: String, callback: SignUpCallback) {
    val user = ParseUser()
    user.put("password", UUID.randomUUID().toString())
    user.put("phone", phone)
    user.put("username", userName)

    user.signUpInBackground(callback)
  }

  fun signInUser(number: String, callback: LogInCallback) {
    val query = ParseUser.getQuery()
    query.whereEqualTo("phone", number)
    ParseCloud.callFunctionInBackground("getUserSessionToken",
        hashMapOf("phoneNumber" to "17017301869"),
        FunctionCallback<String> { token, _ ->
          ParseUser.becomeInBackground(token, callback)
        })
  }

  fun queryUser(field: String, value: String, userExists: (name: String) -> Unit,
                newUser: () -> Unit, error: () -> Unit) {
    val query = ParseUser.getQuery()
    query.whereEqualTo(field, value)
    query.findInBackground { objects, e ->
      when {
        e != null -> {
          // The query was unsuccessful.
          error()
        }
        objects.count() == 0 -> {
          // No users with this phone
          newUser()
        }
        else -> {
          // User with this phone

          //TODO log in user

          userExists(objects.first().username)
        }
      }
    }
  }

}
