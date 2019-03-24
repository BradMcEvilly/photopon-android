package co.photopon.main.gifts

import android.util.Log
import com.parse.ParseFile
import com.parse.ParseObject
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.*

fun ParseObject.toGiftViewModel(shares: Int = 0): GiftViewModel {
  val title = this["title"] as String
  val description = this["description"] as String
  val expirationDate = this["expiration"] as Date
  val instant = Instant.ofEpochMilli(expirationDate.time)
  val giveToGet = this["givetoget"] as Int
  val company = this["company"] as ParseObject
  val couponImage = company["image"] as? ParseFile

  Log.i("ParseObj.toGiftViewMod", "==========================")
  Log.i("ParseObj.toGiftViewMod", "==========================")
  Log.i("ParseObj.toGiftViewMod", "==========================")
  Log.i("ParseObj.toGiftViewMod", "==========================")
  Log.i("ParseObj.toGiftViewMod", "==========================")
  Log.i("ParseObj.toGiftViewMod", "company = $company")
  Log.i("ParseObj.toGiftViewMod", "cImg = $couponImage")
  Log.i("ParseObj.toGiftViewMod", "==========================")
  Log.i("ParseObj.toGiftViewMod", "==========================")
  Log.i("ParseObj.toGiftViewMod", "==========================")
  Log.i("ParseObj.toGiftViewMod", "==========================")

  return GiftViewModel(title, description, LocalDateTime.ofInstant(instant, ZoneId.systemDefault()), giveToGet, shares, company, couponImage, this)
}

fun List<ParseObject>.toGiftViewModels(): List<GiftViewModel> {
  return map { it.toGiftViewModel() }
}
