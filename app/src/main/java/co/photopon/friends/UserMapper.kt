package co.photopon.friends

import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

fun ParseUser.toUserViewModelFromNested(type: UserViewModelType): UserViewModel {
  return UserViewModel(type,
      objectId,
      username,
      null,
      this,
      null,
      this["image"] as? ParseFile)
}

fun List<ParseUser>.toUserViewModels(type: UserViewModelType): List<UserViewModel> {
  return map { it.toUserViewModelFromNested(type) }
}

fun ParseObject.toUserViewModelFromNested(type: UserViewModelType): UserViewModel {
  val user = this["user2"] as ParseUser
  return UserViewModel(type,
      user.objectId,
      user.username,
      user["phone"] as? String,
      user,
      null,
      user["image"] as? ParseFile)
}

fun ParseObject.toUserViewModel(type: UserViewModelType): UserViewModel {
  val user = this as ParseUser
  return UserViewModel(type,
      user.objectId,
      user.username,
      user["phone"] as? String,
      user,
      null,
      user["image"] as? ParseFile)
}

fun List<ParseObject>.toUserViewModelsFromObject(
    type: UserViewModelType = UserViewModelType.USER_PHOTOPON): List<UserViewModel> {
  return map { it.toUserViewModelFromNested(type) }
}

