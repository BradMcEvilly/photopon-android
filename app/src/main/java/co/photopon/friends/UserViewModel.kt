package co.photopon.friends

import android.os.Parcelable
import com.parse.ParseFile
import com.parse.ParseUser
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserViewModel(val type: UserViewModelType,
                         val id: String,
                         val fullName: String,
                         val extendedInfo: String? = null,
                         val parseUser: ParseUser? = null,
                         val localAvatarUri: String? = null,
                         val parseAvatar: ParseFile? = null): Parcelable

enum class UserViewModelType {
  USER_PHOTOPON,
  USER_FRIEND,
  USER_CONTACT,
  USER_PENDING
}