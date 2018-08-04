package co.photopon.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import co.photopon.R
import co.photopon.application.PhotoponActivity
import co.photopon.friends.AddFriendActivity
import co.photopon.main.draw.DrawCouponActivity
import co.photopon.main.draw.DrawCouponActivity.Companion.REQUEST_SHARE_COUPON
import co.photopon.main.friends.MyFriendsFragment
import co.photopon.main.gifts.GiftViewModel
import co.photopon.main.gifts.GiftsFragment
import co.photopon.main.make.MakePhotoponFragment
import co.photopon.main.notifications.NotificationsFragment
import co.photopon.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : PhotoponActivity(), MakePhotoponFragment.MakePhotoponListener,
    NotificationsFragment.NotificationsListener, MyFriendsFragment.MyFriendsListener,
    GiftsFragment.GiftsListener, WalletFragment.WalletListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val selectedPage = intent.getIntExtra(EXTRA_PAGE, PAGE_PHOTOPON)

    val adapter = MainViewPagerAdapter(supportFragmentManager, intent.getParcelableExtra(EXTRA_COUPON))
    view_pager.adapter = adapter
    view_pager.currentItem = Int.MAX_VALUE / 5 + selectedPage

    ib_settings.setOnClickListener { onSettingsClicked() }
  }

  override fun isFullscreen(): Boolean {
    return true
  }

  override fun onBackPressed() {
    if (drawer_layout.isDrawerOpen(Gravity.START)) {
      closeDrawer()
    }
  }

  override fun onCreatePhotoponClicked(coupon: GiftViewModel?) {
    startActivity(getIntent(this, PAGE_PHOTOPON, coupon))

    slideLeftIn()

    finishAffinity()
  }

  override fun onPictureTaken(file: File, coupon: GiftViewModel?) {
    startActivityForResult(DrawCouponActivity.getIntent(this, file, coupon), REQUEST_SHARE_COUPON)
    slideUp()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == REQUEST_SHARE_COUPON && resultCode == RESULT_OK) {
      showOkDialog(R.string.app_name, R.string.share_gift_success)
    }
  }

  override fun onCameraError() {
//    showOkDialog(R.string.generic_error_title, R.string.camera_error)
  }

  override fun onAddFriendClicked() {
    startActivity(AddFriendActivity.getIntent(this))
    slideUp()
  }

  override fun onHamburgerClicked() {
    openDrawer()
  }

  override fun onStorageError() {
    showOkDialog(R.string.generic_error_title, R.string.storage_error)
  }

  private fun openDrawer() {
    drawer_layout.openDrawer(Gravity.START)
  }

  private fun closeDrawer() {
    drawer_layout.closeDrawer(Gravity.START)
  }

  private fun onSettingsClicked() {
    closeDrawer()
    startActivity(SettingsActivity.getIntent(this))
    slideUp()
  }

  companion object {
    const val PAGE_PHOTOPON = 1

    const val EXTRA_PAGE = "extraPage"
    const val EXTRA_COUPON = "extraCoupon"

    fun getIntent(context: Context, selected: Int? = null, coupon: GiftViewModel? = null): Intent {
      val intent = Intent(context, MainActivity::class.java)

      selected?.let { intent.putExtra(EXTRA_PAGE, it) }
      coupon?.let { intent.putExtra(EXTRA_COUPON, it) }

      return intent
    }
  }
}
