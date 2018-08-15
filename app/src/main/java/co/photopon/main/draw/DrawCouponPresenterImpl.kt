package co.photopon.main.draw

import android.content.Context
import co.photopon.managers.GiftManager
import com.parse.ParseFile
import java.io.File


class DrawCouponPresenterImpl(override val view: DrawCouponView,
                              override val context: Context,
                              override val giftManager: GiftManager) : DrawCouponPresenter {

  override fun saveImages(background: File, overlay: File,
                          completion: (success: Boolean, photo: ParseFile, drawing: ParseFile) -> Unit) {
    giftManager.saveImage(background) { success, photo ->
      giftManager.saveImage(overlay) { success, drawing ->
        completion(true, photo, drawing)
      }
    }
  }


}
