package co.photopon.extensions

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

fun LocalDateTime.formatDate(): String {
  val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
  return this.format(formatter)
}