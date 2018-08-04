package co.photopon.managers

import com.parse.ParseObject
import com.parse.SaveCallback

class SmsManager {

  fun sendCode(number: String, code: String, callback: SaveCallback) {
    val verification = ParseObject("Verifications")
    verification.put("code", code)
    verification.put("phoneNumber", number)
    verification.put("numTried", 0)

    verification.saveInBackground(callback)
  }

}
