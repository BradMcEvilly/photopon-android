package co.photopon.main.friends

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
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

  private var TAG: String? = "MyFriendsAdapter"

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(context)
    return ViewHolder(inflater.inflate(R.layout.list_friend, parent, false))
  }

  override fun getItemCount(): Int {
    return friends.count()
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    Log.i(TAG, "--------------------")
    Log.i(TAG, "onBindViewHolder")
    Log.i(TAG, "--------------------")

    Log.i(TAG, "--------------------")
    Log.i(TAG, "onBindViewHolder() — position = $position")
    Log.i(TAG, "--------------------")

    val item = friends[position]
    //friends?.get(position).let { item ->

      Log.i(TAG, "friends?.get(position).let { item -> ...  item = $item")

      holder.btFriend.setOnClickListener { presenter.friendClicked(friends[position]) }

      //Header visibilities
      if (position == 0 || friends[position - 1].type != item.type) {

        Log.i(TAG, "--------------------")
        Log.i(TAG, "onBindViewHolder...if (position == 0 || friends[position - 1].type != item.type) {")
        Log.i(TAG, "--------------------")


        holder.tvHeader.visibility = View.VISIBLE
      } else {

        Log.i(TAG, "--------------------")
        Log.i(TAG, "onBindViewHolder...if (position == 0 || friends[position - 1].type != item.type) {... } else {")
        Log.i(TAG, "--------------------")

        holder.tvHeader.visibility = View.GONE
      }

      //Shares and uses
      holder.tvExtendedInfo.text = item.extendedInfo

      //Header text
      holder.tvHeader.text = getHeaderText(item.type)

      //Full name
      holder.tvFullname.text = item.fullName

      //Load profile pic from address book
      item.localAvatarUri?.let { uri ->

        Log.i(TAG, "--------------------")
        Log.i(TAG, "item.localAvatarUri?.let { uri -> uri = $uri")
        Log.i(TAG, "--------------------")

        holder.ivPerson.setPadding(0, 0, 0, 0)
        GlideApp.with(context!!)
                .load(uri)
                .circleCrop()
                .into(holder.ivPerson)
      }

      Log.i(TAG, "--------------------")
      Log.i(TAG, "item.parseAvatar = ${item.parseAvatar}")
      Log.i(TAG, "--------------------")

      //Load profile pic from parse
      item.parseAvatar?.let { file ->

        Log.i(TAG, "--------------------")
        Log.i(TAG, "------> item.parseAvatar?.let { file -> file = $file")
        Log.i(TAG, "--------------------")

        file.getDataInBackground { data, e ->
          Log.i(TAG, "--------------------")
          Log.i(TAG, "--------------------")
          Log.i(TAG, "------> file.getDataInBackground { data, e -> e = $e")
          Log.i(TAG, "------> file.getDataInBackground { data, e -> data = $data")
          Log.i(TAG, "--------------------")
          if (e == null && data != null && data.isNotEmpty()) {

            Log.i(TAG, "--------------------")
            Log.i(TAG, "file.getDataInBackground { data, e -> if (e == null && data != null && data.isNotEmpty()) {")

            Log.i(TAG, "file.getDataInBackground { data, e -> context = $context")
            Log.i(TAG, "--------------------")

            holder.ivPerson.setPadding(0, 0, 0, 0)
            GlideApp.with(context!!)
                    .asBitmap()
                    .load(data)
                    .fitCenter()
                    .circleCrop()
                    .into(holder.ivPerson)
          }
        }
      }
    //}

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