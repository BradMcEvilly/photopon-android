package co.photopon.extensions

import android.content.Context
import android.util.TypedValue
import java.util.*

fun Random.nextInt(range: IntRange): Int {
  return range.start + nextInt(range.last - range.start)
}

fun String.numbersOnly(): String {
  return replace("[^?0-9]+".toRegex(), "")
}

fun Float.fromDpsToPixels(context: Context): Float {
  return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
}
