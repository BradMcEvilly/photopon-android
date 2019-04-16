package co.photopon.main.draw

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import co.photopon.R
import co.photopon.application.GlideApp
import co.photopon.application.PhotoponActivity
import co.photopon.extensions.fromDpsToPixels
import co.photopon.extensions.mainApplication
import co.photopon.main.gifts.GiftViewModel
import co.photopon.share.ShareGiftActivity
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.activity_draw_coupon.*
import kotlinx.android.synthetic.main.popup_color.view.*
import kotlinx.android.synthetic.main.popup_stroke.view.*
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class DrawCouponActivity : PhotoponActivity(), DrawCouponView {

  private var strokePopup: PopupWindow? = null
  private var colorPopup: PopupWindow? = null

  private var backgroundFile: File? = null

  private val TAG: String = "DrawCouponActivity"

  @Inject
  lateinit var presenter: DrawCouponPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    UXCam.startWithKey("1p78vfc6zdrv4cx")

    setContentView(R.layout.activity_draw_coupon)

    mainApplication.appComponent().plus(DrawCouponModule(this)).inject(this)

    view_choose_stroke.setOnClickListener { showStrokePopup() }
    view_choose_color.setOnClickListener { showColorPopup() }
    ib_close.setOnClickListener { onCloseClicked() }
    ib_done.setOnClickListener { onDoneClicked() }

    setColor(R.color.basic_blue)
    setStrokeWidth(STROKE_THINNEST)

    initializeLoading()
    initializeImageView()
    initializeCoupon()
  }

  override fun displayLoading() {
  }

  override fun finishedLoading() {
  }

  override fun isFullscreen(): Boolean {
    return true
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == REQUEST_SHARE_COUPON && resultCode == RESULT_OK) {
      setResult(RESULT_OK)
      finish()
      overridePendingTransition(R.anim.stay, R.anim.stay)
    }
  }

  private fun initializeCoupon() {
    intent.getParcelableExtra<GiftViewModel>(EXTRA_VIEW_MODEL)?.let { viewModel ->
      tv_coupon_title.text = viewModel.title
      tv_coupon_description.text = viewModel.description

      if (viewModel.couponImage != null) {

        Log.i(TAG, "-----------------------")
        Log.i(TAG, "if (viewModel.couponImage != null) {")
        Log.i(TAG, "-----------------------")

        viewModel.couponImage.let { file ->

          Log.i(TAG, "--------------------")
          Log.i(TAG, "couponImage.let { file -> file = $file")
          Log.i(TAG, "--------------------")

          file.getDataInBackground { fileData, e ->

            Log.i(TAG, "--------------------")
            Log.i(TAG, "--------------------")
            Log.i(TAG, "couponImage.let { file -> file = $file")
            Log.i(TAG, "--------------------")
            Log.i(TAG, "--------------------")

            Log.i(TAG, "--------------------")
            Log.i(TAG, "file.getDataInBackground { fileData, e -> e = $e")
            Log.i(TAG, "--------------------")
            //Log.i(TAG, "file.getDataInBackground { data, e -> data = $data")
            Log.i(TAG, "--------------------")

            if (e == null && fileData != null && fileData.isNotEmpty()) {

              Log.i(TAG, "------>  if (e == null && data != null && data.isNotEmpty()) {")
              // here we g0\
              iv_coupon.setPadding(0, 0, 0, 0)

              Log.i(TAG, "-----------------------")
              Log.i(TAG, "iv_coupon.setpadding...")
              Log.i(TAG, "-----------------------")

              GlideApp.with(this)
                      .asBitmap()
                      .load(fileData)
                      .fitCenter()
                      .circleCrop()
                      .into(iv_coupon)

              Log.i(TAG, "-----------------------")
              Log.i(TAG, "GlideApp.with(this)...)")
              Log.i(TAG, "-----------------------")

            }

          }
        }
      }

    }
  }

  private fun initializeImageView() {
    backgroundFile = intent.getSerializableExtra(EXTRA_FILE) as File
    ink_view.background = Drawable.createFromPath(backgroundFile?.absolutePath)
  }

  private fun saveImage() {
    val bmp = ink_view.getBitmap(ContextCompat.getColor(this, android.R.color.transparent))
    val stream = FileOutputStream("$filesDir/photopon.png")
    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
    stream.close()
  }

  private fun onCloseClicked() {
    finish()
    slideDown()
  }

  private fun onDoneClicked() {
    saveImage()
    presenter.saveImages(backgroundFile!!,
        File("$filesDir/photopon.png")) { success, photo, drawing ->
      if (success) {
        val coupon = intent.getParcelableExtra<GiftViewModel>(EXTRA_VIEW_MODEL)
        startActivityForResult(
            ShareGiftActivity.getIntent(this, coupon.parseObject, drawing, photo),
            REQUEST_SHARE_COUPON)
        slideUp()
      }
    }
  }

  private fun initializeLoading() {
    tv_loading.text = getString(R.string.draw_uploading)
    tv_loading.visibility = View.GONE
    progress_bar.visibility = View.GONE
  }

  private fun showLoading() {
    tv_loading.visibility = View.VISIBLE
    progress_bar.visibility = View.VISIBLE
  }

  private fun setColor(color: Int) {
    when (color) {
      R.color.basic_black -> view_choose_color.background =
          getDrawable(R.drawable.square_black_border)
      R.color.basic_blue -> view_choose_color.background =
          getDrawable(R.drawable.square_blue_border)
      R.color.basic_brown -> view_choose_color.background =
          getDrawable(R.drawable.square_brown_border)
      R.color.basic_cyan -> view_choose_color.background =
          getDrawable(R.drawable.square_cyan_border)
      R.color.basic_green -> view_choose_color.background =
          getDrawable(R.drawable.square_green_border)
      R.color.basic_orange -> view_choose_color.background =
          getDrawable(R.drawable.square_orange_border)
      R.color.basic_purple -> view_choose_color.background =
          getDrawable(R.drawable.square_purple_border)
      R.color.basic_red -> view_choose_color.background =
          getDrawable(R.drawable.square_red_border)
      R.color.basic_white -> view_choose_color.background =
          getDrawable(R.drawable.square_white_border)
      R.color.basic_yellow -> view_choose_color.background =
          getDrawable(R.drawable.square_yellow_border)
    }
    ink_view.setColor(ContextCompat.getColor(this, color))

    colorPopup?.dismiss()
    colorPopup = null
  }

  private fun setStrokeWidth(width: Int) {
    val min: Float?
    val max: Float?

    when (width) {
      STROKE_THINNEST -> {
        view_choose_stroke.background = getDrawable(R.drawable.stroke_thinnest)
        min = 0.5f
        max = 1.0f
      }
      STROKE_THIN -> {
        view_choose_stroke.background = getDrawable(R.drawable.stroke_thin)
        min = 1.0f
        max = 2.0f
      }
      STROKE_THICK -> {
        view_choose_stroke.background = getDrawable(R.drawable.stroke_thick)
        min = 1.5f
        max = 3.0f
      }
      STROKE_THICKEST -> {
        view_choose_stroke.background = getDrawable(R.drawable.stroke_thickest)
        min = 2.0f
        max = 4.0f
      }
      else -> throw RuntimeException("Invalid Stroke Width.")
    }

    ink_view.setMaxStrokeWidth(max.fromDpsToPixels(this))
    ink_view.setMinStrokeWidth(min.fromDpsToPixels(this))

    strokePopup?.dismiss()
    strokePopup = null
  }

  private fun showColorPopup() {
    val inflater = LayoutInflater.from(this)
    val view = inflater.inflate(R.layout.popup_color, null)

    view.view_blue.setOnClickListener { setColor(R.color.basic_blue) }
    view.view_black.setOnClickListener { setColor(R.color.basic_black) }
    view.view_purple.setOnClickListener { setColor(R.color.basic_purple) }
    view.view_brown.setOnClickListener { setColor(R.color.basic_brown) }
    view.view_red.setOnClickListener { setColor(R.color.basic_red) }
    view.view_green.setOnClickListener { setColor(R.color.basic_green) }
    view.view_orange.setOnClickListener { setColor(R.color.basic_orange) }
    view.view_cyan.setOnClickListener { setColor(R.color.basic_cyan) }
    view.view_yellow.setOnClickListener { setColor(R.color.basic_yellow) }
    view.view_white.setOnClickListener { setColor(R.color.basic_white) }

    colorPopup = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)
    colorPopup?.isOutsideTouchable = true
    colorPopup?.isFocusable = true
    colorPopup?.showAsDropDown(layout_draw_controls, 0, -layout_draw_controls.height * 6)
  }

  private fun showStrokePopup() {
    val inflater = LayoutInflater.from(this)
    val view = inflater.inflate(R.layout.popup_stroke, null)

    view.view_thickest.setOnClickListener { setStrokeWidth(STROKE_THICKEST) }
    view.view_thick.setOnClickListener { setStrokeWidth(STROKE_THICK) }
    view.view_thin.setOnClickListener { setStrokeWidth(STROKE_THIN) }
    view.view_thinnest.setOnClickListener { setStrokeWidth(STROKE_THINNEST) }

    strokePopup = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)
    strokePopup?.isOutsideTouchable = true
    strokePopup?.isFocusable = true
    strokePopup?.showAsDropDown(layout_draw_controls, layout_draw_controls.width,
        -layout_draw_controls.height * 5)
  }

  companion object {
    const val STROKE_THINNEST = 1423
    const val STROKE_THIN = 2342
    const val STROKE_THICK = 3903
    const val STROKE_THICKEST = 4158

    const val EXTRA_FILE = "extraFile"
    const val EXTRA_VIEW_MODEL = "extraFileViewModel"

    const val REQUEST_SHARE_COUPON = 59221

    fun getIntent(context: Context, file: File, viewModel: GiftViewModel?): Intent {
      val intent = Intent(context, DrawCouponActivity::class.java)
      intent.putExtra(EXTRA_FILE, file)
      intent.putExtra(EXTRA_VIEW_MODEL, viewModel)
      return intent
    }
  }
}