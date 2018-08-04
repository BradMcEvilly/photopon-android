package co.photopon.main.gifts

import com.parse.ParseObject
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.*


/*
0 = {java.util.HashMap$Node@6046} "owner" ->
1 = {java.util.HashMap$Node@6047} "code" -> "photo123"
2 = {java.util.HashMap$Node@6048} "oneperuser" -> "true"
3 = {java.util.HashMap$Node@6049} "description" -> "Get a free ride with a keg of beer"
4 = {java.util.HashMap$Node@6050} "givetoget" -> "5"
5 = {java.util.HashMap$Node@6051} "expiration" -> "Sat Jun 02 11:46:31 CDT 2018"
6 = {java.util.HashMap$Node@6052} "locations" -> " size = 2"
7 = {java.util.HashMap$Node@6053} "company" ->
8 = {java.util.HashMap$Node@6054} "title" -> "ferrari ride"
9 = {java.util.HashMap$Node@6055} "isActive" -> "true"
 */

fun ParseObject.toGiftViewModel(shares: Int = 0): GiftViewModel {
  val title = this["title"] as String
  val description = this["description"] as String
  val expirationDate = this["expiration"] as Date
  val instant = Instant.ofEpochMilli(expirationDate.time)
  val giveToGet = this["givetoget"] as Int

  return GiftViewModel(title, description, LocalDateTime.ofInstant(instant, ZoneId.systemDefault()), giveToGet, shares, this)
}

fun List<ParseObject>.toGiftViewModels(): List<GiftViewModel> {
  return map { it.toGiftViewModel() }
}
