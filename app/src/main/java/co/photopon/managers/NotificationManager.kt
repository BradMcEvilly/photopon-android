package co.photopon.managers

import android.util.Log
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser

class NotificationManager {

  private val TAG: String = "NotificationManager"

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

  fun createAddFriendNotification(toUser: ParseUser){
    // query first because for this, we only ever need to have a single notification

  }

  fun removeAddFriendNotification(userToRemove: ParseUser){
    // query first because for this, we only ever need to have a single notification

  }

  fun createMessageNotification(toUser: ParseUser, content: String){

    val notification = ParseObject("Notifications")
    notification.put("to", toUser)
    notification.put("content", content)
    notification.put("assocUser", ParseUser.getCurrentUser())
    notification.put("type", "MESSAGE")
    notification.saveInBackground()
  }

  fun createViewPhotoponNotification(toUser: ParseUser, photopon: ParseObject){

    val notification = ParseObject("Notifications")
    notification.put("to", toUser)
    notification.put("assocPhotopon", photopon)
    notification.put("assocUser", ParseUser.getCurrentUser())
    notification.put("type", "VIEWED")
    notification.saveInBackground()
  }
//
//  fun createPhotoponNotificationsForUsers(toUsers: <ParseUser>, photopon: ParseObject){
//
//  }

  fun createPhotoponNotification(toUser: ParseUser, photopon: ParseObject) {

    Log.i(TAG, "-----------------------------------")
    Log.i(TAG, "photopon.saveInBackground { e ->  BEGIN")
    Log.i(TAG, "-----------------------------------")

    val notification = ParseObject("Notifications")


    notification.put("to", toUser)
    notification.put("assocPhotopon", photopon)
    notification.put("assocUser", ParseUser.getCurrentUser())
    notification.put("type", "PHOTOPON")
    notification.saveInBackground({ e ->
      Log.i(TAG, "-----------------------------------")
      Log.i(TAG, "notification.saveInBackground({ e ->")
      Log.i(TAG, "-----------------------------------")

      if(e == null){
        Log.i(TAG, "-----------------------------------")
        Log.i(TAG, "notification.saveInBackground   SUCCESS!!")
        Log.i(TAG, "-----------------------------------")
      } else {
        Log.i(TAG, "-----------------------------------")
        Log.i(TAG, "notification.saveInBackground(  FAILED!!")
        Log.i(TAG, "-----------------------------------")
      }

    })


//    photopon.increment("numShared", 1)
//    photopon.saveInBackground()
  }

  fun createAddWalletNotification(toUser: ParseUser, photopon: ParseObject){

    val notification = ParseObject("Notifications")
    notification.put("to", toUser)
    notification.put("assocPhotopon", photopon)
    notification.put("assocUser", ParseUser.getCurrentUser())
    notification.put("type", "ADDWALLET")
    notification.saveInBackground()
  }

  fun createUnlockedCouponNotification(toUser: ParseUser, photopon: ParseObject){

    val notification = ParseObject("Notifications")
    notification.put("to", toUser)
    notification.put("assocPhotopon", photopon)
    notification.put("assocUser", ParseUser.getCurrentUser())
    notification.put("type", "UNLOCKEDCOUPON")
    notification.saveInBackground()
  }

  fun createRedeemedUnlockedCouponNotification(toUser: ParseUser, photopon: ParseObject){

    val notification = ParseObject("Notifications")
    notification.put("to", toUser)
    notification.put("assocPhotopon", photopon)
    notification.put("assocUser", ParseUser.getCurrentUser())
    notification.put("type", "REDEEMEDUNLOCKED")
    notification.saveInBackground()
  }

  fun createRedeemedNotification(toUser: ParseUser, photopon: ParseObject){

    val notification = ParseObject("Notifications")
    notification.put("to", toUser)
    notification.put("assocPhotopon", photopon)
    notification.put("assocUser", ParseUser.getCurrentUser())
    notification.put("type", "REDEEMED")
    notification.saveInBackground()
  }

}
