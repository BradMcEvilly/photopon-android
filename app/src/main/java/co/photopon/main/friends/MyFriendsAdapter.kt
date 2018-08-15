package co.photopon.main.friends

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import co.photopon.application.GlideApp
import co.photopon.friends.UserViewModel
import co.photopon.friends.UserViewModelType
import kotlinx.android.synthetic.main.list_friend.view.*


class MyFriendsAdapter(val context: Context,
                       val presenter: MyFriendsPresenter,
                       val friends: List<UserViewModel>) :
    RecyclerView.Adapter<MyFriendsAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(context)
    return ViewHolder(inflater.inflate(R.layout.list_friend, parent, false))
  }

  override fun getItemCount(): Int {
    return friends.count()
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = friends[position]

    holder.btFriend.setOnClickListener { presenter.friendClicked(friends[position]) }

    //Header visibilities
    if (position == 0 || friends[position - 1].type != item.type) {
      holder.tvHeader.visibility = View.VISIBLE
    } else {
      holder.tvHeader.visibility = View.GONE
    }

    //Shares and uses
    holder.tvExtendedInfo.text = item.extendedInfo

    //Header text
    holder.tvHeader.text = getHeaderText(item.type)

    //Full name
    holder.tvFullname.text = item.fullName

    //Load profile pic from parse
    item.parseAvatar?.let { file ->
      file.getDataInBackground { data, e ->
        if (e == null && data != null && data.isNotEmpty()) {
          holder.ivPerson.setPadding(0, 0, 0, 0)
          GlideApp.with(context)
              .asBitmap()
              .load(data)
              .fitCenter()
              .circleCrop()
              .into(holder.ivPerson)
        }
      }
    }
  }

  private fun getHeaderText(type: UserViewModelType): String? {
    return when (type) {
      UserViewModelType.USER_FRIEND -> context.getString(R.string.my_friends_friends)
      UserViewModelType.USER_PENDING -> context.getString(R.string.my_friends_pending)
      else -> return null
    }
  }

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvHeader = view.tv_friend_header!!
    val tvFullname = view.tv_full_name!!
    val tvExtendedInfo = view.tv_friend_info!!
    val ivPerson = view.iv_person!!
    val btFriend = view.bt_friend!!
  }

}