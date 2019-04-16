package co.photopon.share

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import co.photopon.R
import co.photopon.application.PhotoponActivity
import co.photopon.extensions.mainApplication
import co.photopon.friends.*
import com.parse.ParseFile
import com.parse.ParseObject
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.activity_share_gift.*
import javax.inject.Inject

class ShareGiftActivity : PhotoponActivity(), ShareGiftView {

  @Inject
  lateinit var presenter: ShareGiftPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    UXCam.startWithKey("1p78vfc6zdrv4cx")

    setContentView(R.layout.activity_share_gift)

    mainApplication.appComponent().plus(ShareGiftModule(this)).inject(this)

    ib_close.setOnClickListener { onBackPressed() }
    swipe_refresh.setOnRefreshListener { presenter.onRefresh() }
    ib_check_circle.setOnClickListener { onShareClicked() }
    bt_add_friend.setOnClickListener { onAddFriendClicked() }

    presenter.onRefresh()
    swipe_refresh.isRefreshing = true
  }

  override fun onResume() {
    super.onResume()
    presenter.onRefresh()
  }

  override fun isFullscreen(): Boolean {
    return true
  }

  override fun onBackPressed() {
    finish()
    slideDown()
  }

  override fun displayFriends(friends: List<UserViewModel>) {
    swipe_refresh.isRefreshing = false
    val adapter = ShareGiftAdapter(this, friends)
    val linearLayoutManager = LinearLayoutManager(this)
    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
    recycler_view.layoutManager = linearLayoutManager
    recycler_view.adapter = adapter

    layout_no_friends.visibility = if (friends.isEmpty()) View.VISIBLE else View.GONE
  }

  override fun clearList() {
    recycler_view.adapter = ShareGiftAdapter(this, arrayListOf())
  }

  override fun onShareClicked() {
    val adapter = recycler_view.adapter as ShareGiftAdapter
    presenter.share(adapter.getSelectedItems(),
        intent.getParcelableExtra(INTENT_COUPON) as ParseObject,
        intent.getParcelableExtra(INTENT_PHOTO) as ParseFile,
        intent.getParcelableExtra(INTENT_DRAWING) as ParseFile) {
      setResult(RESULT_OK)
      finish()
      slideDown()
    }
  }

  override fun displayShared() {
    showOkDialog(R.string.app_name, R.string.share_gift_success) {
      finish()
      slideDown()
    }
  }

  private fun onAddFriendClicked() {
    startActivity(AddFriendActivity.getIntent(this))
    slideUp()
  }

  companion object {
    private const val INTENT_COUPON = "intentCoupon"
    private const val INTENT_PHOTO = "intentPhoto"
    private const val INTENT_DRAWING = "intentDrawing"

    fun getIntent(context: Context, coupon: ParseObject, drawing: ParseFile,
                  photo: ParseFile): Intent {
      val intent = Intent(context, ShareGiftActivity::class.java)
      intent.putExtra(INTENT_COUPON, coupon)
      intent.putExtra(INTENT_DRAWING, drawing)
      intent.putExtra(INTENT_PHOTO, photo)

      return intent
    }
  }
}