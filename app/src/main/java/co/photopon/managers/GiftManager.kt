package co.photopon.managers

import com.parse.*
import java.io.File
import android.util.Log

class GiftManager {

  private val notificationManager:NotificationManager = NotificationManager()
  private val TAG: String = "GiftManager"

  fun saveImage(image: File, completion: (success: Boolean, file: ParseFile) -> Unit) {
    val file = ParseFile(image)
    file.saveInBackground(SaveCallback { e -> completion(e == null, file) })
  }

  fun sendPhotopons(coupon: ParseObject, drawing: ParseFile, photo: ParseFile,
                    friends: List<ParseUser>, completion: (success: Boolean) -> Unit) {

    Log.i(TAG, "sendPhotopons")

    val photopon = ParseObject("Photopon")

    photopon.put("drawing", drawing)
    photopon.put("photo", photo)
    photopon.put("coupon", coupon)
    photopon.put("creator", ParseUser.getCurrentUser())
    photopon.put("users", friends.map { it.objectId })
    photopon.put("installationId", "b406885f-0b8f-4e66-ab80-19681074362d")

    photopon.saveInBackground { e ->

      Log.i(TAG, "-----------------------------------")
      Log.i(TAG, "photopon.saveInBackground { e ->  BEGIN")
      Log.i(TAG, "-----------------------------------")

      if(e == null){

        Log.i(TAG, "-----------------------------------")
        Log.i(TAG, "photopon.saveInBackground SUCCESS")
        Log.i(TAG, "-----------------------------------")

        friends.forEach({ u ->
          Log.i(TAG, "-----------------------------------")
          Log.i(TAG, "for (u in users) { u =(BEFORE TYPE CAST) $u")
          Log.i(TAG, "-----------------------------------")

//          Log.i(TAG, "-----------------------------------")
//          Log.i(TAG, "for (u in users) { u =(AFTER TYPE CAST) $u")
//          Log.i(TAG, "-----------------------------------")

          notificationManager.createPhotoponNotification(u, photopon)

        })

      }
      completion(e == null)

      Log.i(TAG, "-----------------------------------")
      Log.i(TAG, "photopon.saveInBackground { e ->  END!!!!")
      Log.i(TAG, "-----------------------------------")

    }
  }

  fun getCoupons(coupons: (coupons: List<ParseObject>) -> Unit): ParseQuery<ParseObject> {
    val query = ParseQuery<ParseObject>("Coupon")
    query.include("company")

    query.findInBackground { objects, e ->
      if (e == null) {



        coupons(objects)
      } else {
        println("------------------------")
        println("query.findInBackground { objects, e ->")
        println("ERROR: ")
        println(e.toString())
        println("------------------------")
      }
    }
    return query
  }

  fun getCouponShares(coupon: ParseObject, shares: (shares: Int) -> Unit): ParseQuery<ParseObject> {
    val query = ParseQuery<ParseObject>("PerUserShare")
    query.include("user")
    query.include("coupon")
    query.include("friend")

    query.whereEqualTo("user", ParseUser.getCurrentUser())
    query.whereEqualTo("coupon", coupon)

    query.findInBackground { shares, e ->
      if (e == null) {
        shares(shares.count())
      } else {
//        error
      }
    }
    return query
  }
}
