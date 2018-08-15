package co.photopon.managers

import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser

class NotificationManager {
  fun getNotifications(notifications: (notifications: List<ParseObject>) -> Unit) {
    val query = ParseQuery<ParseObject>("Notifications")
    query.include("to")
    query.include("assocUser")
    query.include("assocPhotopon")
    query.include("assocPhotopon.coupon")
    query.include("assocPhotopon.coupon.company")

    query.whereEqualTo("to", ParseUser.getCurrentUser())

    query.findInBackground { notifications, e ->
      if (e == null) {
        notifications(notifications)
      } else {
        //error
      }
    }
  }

  fun deleteNotification(notification: ParseObject, complete: () -> Unit) {
    notification.deleteInBackground {
      complete()
    }
  }

  fun createPhotoponNotification(toUser: ParseUser, photopon: ParseObject) {
    val notification = ParseObject.create("Notifications")

    notification.put("to", toUser)
    notification.put("assocPhotopon", photopon)
    notification.put("assocUser", ParseUser.getCurrentUser())
    notification.put("type", "PHOTOPON")

    notification.saveInBackground()

    photopon.increment("numShared", 1)
    photopon.saveInBackground()
  }
}
