package co.photopon.main.gifts

import android.os.Parcelable
import com.parse.ParseFile
import com.parse.ParseObject
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime

@Parcelize
data class GiftViewModel(val title: String,
                         val description: String,
                         val expires: LocalDateTime,
                         val giveToGet: Int,
                         val shares: Int,
                         val company: ParseObject,
                         val couponImage: ParseFile? = null,
                         val parseObject: ParseObject): Parcelable
