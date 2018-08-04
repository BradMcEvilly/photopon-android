package co.photopon.share

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import co.photopon.application.GlideApp
import co.photopon.friends.UserViewModel
import kotlinx.android.synthetic.main.list_share_gift.view.*


class ShareGiftAdapter(val context: Context,
                       val friends: List<UserViewModel>) :
    RecyclerView.Adapter<ShareGiftAdapter.ViewHolder>() {

  private var selectedFriends = arrayListOf<UserViewModel>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(context)
    return ViewHolder(inflater.inflate(R.layout.list_share_gift, parent, false))
  }

  override fun getItemCount(): Int {
    return friends.count()
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = friends[position]

    //Header visibilities
    if (position == 0) {
      holder.tvHeader.visibility = View.VISIBLE
    } else {

      val previous = friends[position - 1].fullName[0].toLowerCase()
      val current = item.fullName[0].toLowerCase()

      holder.tvHeader.visibility = if (previous != current) View.VISIBLE else View.GONE
    }

    //Header text
    holder.tvHeader.text = getHeaderText(item)

    //Full name
    holder.tvFullname.text = item.fullName

    //Selection
    holder.check_box.setOnCheckedChangeListener({ _, b ->
      if (b) {
        selectedFriends.add(item)
      } else {
        selectedFriends.remove(item)
      }
    })

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

  fun getSelectedItems(): List<UserViewModel> {
    return selectedFriends
  }

  private fun getHeaderText(friend: UserViewModel): String {
    return friend.fullName[0].toUpperCase().toString()
  }

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvHeader = view.tv_share_header!!
    val check_box = view.cb_selected!!
    val tvFullname = view.tv_full_name!!
    val ivPerson = view.iv_person!!
  }

}