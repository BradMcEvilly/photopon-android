package co.photopon.main.draw

import android.content.Context
import co.photopon.managers.GiftManager
import com.parse.ParseFile
import java.io.File

interface DrawCouponView {
  fun displayLoading()

  fun finishedLoading()
}

interface DrawCouponPresenter {
  val view: DrawCouponView

  val context: Context

  val giftManager: GiftManager

  fun saveImages(background: File, overlay: File,
                 completion: (success: Boolean, photo: ParseFile, drawing: ParseFile) -> Unit)
}
