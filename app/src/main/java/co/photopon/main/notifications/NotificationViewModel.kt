package co.photopon.main.gifts

import android.os.Parcelable
import com.parse.ParseObject
import com.parse.ParseUser
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NotificationViewModel(val type: NotificationViewModelType,
                                 val assocUser: ParseUser, //todo clean up all Parse objects in view models
                                 val to: ParseUser,
                                 val assocPhotopon: ParseObject?) : Parcelable

enum class NotificationViewModelType(s: String) {
  FRIEND("FRIEND"),
  PHOTOPON("PHOTOPON"),
  UNLOCKEDCOUPON("UNLOCKEDCOUPON"),
  REDEEMED("REDEEMED"),
  ADDWALLET("ADDWALLET"),
  MESSAGE("MESSAGE")
}
