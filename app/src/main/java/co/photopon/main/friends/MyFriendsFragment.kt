package co.photopon.main.friends

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import co.photopon.application.GlideApp
import co.photopon.chat.ChatActivity
import co.photopon.extensions.mainApplication
import co.photopon.friends.UserViewModel
import kotlinx.android.synthetic.main.dialog_about_friend.view.*
import kotlinx.android.synthetic.main.fragment_myfriends.*
import kotlinx.android.synthetic.main.fragment_myfriends.view.*
import javax.inject.Inject


class MyFriendsFragment : Fragment(), MyFriendsView {

  interface MyFriendsListener {
    fun onHamburgerClicked()

    fun onAddFriendClicked()
  }

  @Inject
  lateinit var presenter: MyFriendsPresenter
  lateinit var callback: MyFriendsListener

  override fun onAttach(context: Context?) {
    super.onAttach(context)

    callback = context as MyFriendsListener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    mainApplication.appComponent().plus(MyFriendsModule(this)).inject(this)

    val viewGroup = inflater.inflate(R.layout.fragment_myfriends, container, false) as ViewGroup

    viewGroup.ib_hamburger.setOnClickListener { callback.onHamburgerClicked() }
    viewGroup.bt_add_friend.setOnClickListener { callback.onAddFriendClicked() }
    viewGroup.ib_add_friend.setOnClickListener { callback.onAddFriendClicked() }

    viewGroup.swipe_refresh.setOnRefreshListener { presenter.onRefresh() }

    presenter.onRefresh()
    viewGroup.swipe_refresh.isRefreshing = true

    return viewGroup
  }

  override fun displayFriends(friends: List<UserViewModel>) {
    swipe_refresh.isRefreshing = false
    val adapter = MyFriendsAdapter(activity!!, presenter, friends)
    val linearLayoutManager = LinearLayoutManager(activity!!)
    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
    recycler_view.layoutManager = linearLayoutManager
    recycler_view.adapter = adapter

    layout_no_friends.visibility = if (friends.isEmpty()) View.VISIBLE else View.GONE
  }

  override fun displayChat(friend: UserViewModel) {
    startActivity(ChatActivity.getIntent(context!!, friend))
  }

  override fun aboutFriend(friend: UserViewModel) {
    val inflater = LayoutInflater.from(context)
    val layout = inflater.inflate(R.layout.dialog_about_friend, null)

    layout.tv_friend_name.text = friend.fullName
    layout.iv_chat.setOnClickListener { presenter.chatClicked(friend) }

    friend.parseAvatar?.let { file ->
      file.getDataInBackground { data, e ->
        if (e == null && data != null && data.isNotEmpty()) {
          layout.iv_avatar.setPadding(0, 0, 0, 0)
          GlideApp.with(context!!)
              .asBitmap()
              .load(data)
              .fitCenter()
              .circleCrop()
              .into(layout.iv_avatar)
        }
      }
    }

    val builder = AlertDialog.Builder(context!!)
    builder.setView(layout)

    builder.setCancelable(true)

    builder.show()
  }

  override fun clearList() {
    recycler_view.adapter = MyFriendsAdapter(activity!!, presenter, arrayListOf())
  }

}
