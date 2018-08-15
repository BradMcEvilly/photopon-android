package co.photopon.main.gifts

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import co.photopon.application.PhotoponActivity
import co.photopon.application.PhotoponFragment
import co.photopon.extensions.mainApplication
import co.photopon.main.friends.GiftsAdapter
import co.photopon.main.friends.GiftsModule
import co.photopon.main.friends.GiftsPresenter
import co.photopon.main.friends.GiftsView
import com.parse.ParseObject
import kotlinx.android.synthetic.main.fragment_gifts.*
import kotlinx.android.synthetic.main.fragment_gifts.view.*
import javax.inject.Inject


class GiftsFragment : PhotoponFragment(), GiftsView {

  @Inject
  lateinit var presenter: GiftsPresenter
  lateinit var callback: GiftsListener

  interface GiftsListener {
    fun onHamburgerClicked()

    fun onCreatePhotoponClicked(coupon: GiftViewModel? = null)
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as GiftsListener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    mainApplication.appComponent().plus(GiftsModule(this)).inject(this)

    val viewGroup = inflater.inflate(R.layout.fragment_gifts, container, false) as ViewGroup

    viewGroup.ib_hamburger.setOnClickListener { callback.onHamburgerClicked() }
    viewGroup.swipe_refresh.setOnRefreshListener { presenter.onRefresh() }

    presenter.onRefresh()
    viewGroup.swipe_refresh.isRefreshing = true

    return viewGroup
  }

  override fun clearList() {
    recycler_view.adapter = GiftsAdapter(activity!!, presenter, arrayListOf())
  }

  override fun displayGiveCoupon(coupon: GiftViewModel) {
    callback.onCreatePhotoponClicked(coupon)
  }

  override fun displayGifts(gifts: List<GiftViewModel>) {
    swipe_refresh.isRefreshing = false
    val adapter = GiftsAdapter(activity!!, presenter, gifts)
    val linearLayoutManager = LinearLayoutManager(activity!!)
    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
    recycler_view.layoutManager = linearLayoutManager
    recycler_view.adapter = adapter

    tv_no_gifts.visibility = if (gifts.isEmpty()) View.VISIBLE else View.GONE
  }

  override fun displayNotEnoughShares(needed: Int, coupon: ParseObject) {
    val activity = this.activity as PhotoponActivity
    val plural = if (needed == 1) "" else activity.getString(R.string.plural_s)
    val message = activity.getString(R.string.share_gift_not_enough_long, needed, plural)
    showCancelableDialog(R.string.share_gift_not_enough, message, R.string.share_gift_share_now, {
//      startActivity(ShareGiftActivity.getIntent(activity, coupon))
//      activity.slideUp()
    })
  }
}
