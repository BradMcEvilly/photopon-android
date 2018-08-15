package co.photopon.main.notifications

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import co.photopon.chat.ChatActivity
import co.photopon.extensions.mainApplication
import co.photopon.friends.UserViewModel
import co.photopon.main.friends.NotificationsAdapter
import co.photopon.main.friends.NotificationsModule
import co.photopon.main.friends.NotificationsPresenter
import co.photopon.main.friends.NotificationsView
import co.photopon.main.gifts.NotificationViewModel
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*
import javax.inject.Inject


class NotificationsFragment : Fragment(), NotificationsView {

  @Inject
  lateinit var presenter: NotificationsPresenter

  interface NotificationsListener {
    fun onHamburgerClicked()
  }

  lateinit var callback: NotificationsListener

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as NotificationsListener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

    mainApplication.appComponent().plus(NotificationsModule(this)).inject(this)

    val viewGroup = inflater.inflate(R.layout.fragment_notifications, container, false) as ViewGroup

    viewGroup.ib_hamburger.setOnClickListener { callback.onHamburgerClicked() }
    viewGroup.swipe_refresh.setOnRefreshListener { presenter.onRefresh() }

    presenter.onRefresh()

    return viewGroup
  }

  override fun displayNotifications(notifications: List<NotificationViewModel>) {
    swipe_refresh.isRefreshing = false
    val adapter = NotificationsAdapter(activity!!, presenter, notifications)
    val linearLayoutManager = LinearLayoutManager(activity!!)
    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
    recycler_view.layoutManager = linearLayoutManager
    recycler_view.adapter = adapter

    tv_empty_list.visibility = if (notifications.isEmpty()) View.VISIBLE else View.GONE
  }

  override fun clearList() {
    recycler_view.adapter = NotificationsAdapter(activity!!, presenter, arrayListOf())
  }

  override fun displayChat(friend: UserViewModel) {
    startActivity(ChatActivity.getIntent(context!!, friend))
  }
}
