package co.photopon.managers

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager {
  fun putToken(context: Context, token: String) {
    prefs(context).edit().putString(KEY_TOKEN, token).apply()
  }

  fun getToken(context: Context): String? {
    return prefs(context).getString(KEY_TOKEN, null)
  }

  fun nuke(context: Context) {
    prefs(context).edit().clear().apply()
  }

  private fun prefs(context: Context): SharedPreferences {
    return context.getSharedPreferences(FILE_PREFS, Context.MODE_PRIVATE)
  }

  companion object {
    val FILE_PREFS = "photoponPreferences"

    val KEY_TOKEN = "keyToken"
  }
}
