package co.photopon.main.notifications

import co.photopon.main.gifts.NotificationViewModel
import co.photopon.main.gifts.NotificationViewModelType
import com.parse.ParseObject
import com.parse.ParseUser

fun ParseObject.toNotificationViewModel(): NotificationViewModel {
  val type = NotificationViewModelType.valueOf(this["type"] as String)
  val user = this["assocUser"] as ParseUser
  val to = this["to"] as ParseUser
  val assocPhotopon = this["assocPhotopon"] as? ParseObject
  return NotificationViewModel(type, user, to, assocPhotopon)
}

fun List<ParseObject>.toNotificationViewModels(): List<NotificationViewModel> {
  return map { it.toNotificationViewModel() }
}