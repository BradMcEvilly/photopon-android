package co.photopon.friends

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import co.photopon.R
import co.photopon.application.GlideApp
import kotlinx.android.synthetic.main.list_add_contact.view.*


class AddFriendAdapter() :
    RecyclerView.Adapter<AddFriendAdapter.ViewHolder>() {

  private var context: Context? = null
  private var presenter: AddFriendPresenter? = null
  private var items: ArrayList<UserViewModel>? = null

  constructor(context: Context,
              presenter: AddFriendPresenter,
              users: List<UserViewModel>,
              friends: List<UserViewModel>,
              contacts: List<UserViewModel>) : this() {

    items = arrayListOf()
    items?.addAll(users)
    items?.addAll(friends)
    items?.addAll(contacts)

    this.context = context
    this.presenter = presenter
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(context)
    return ViewHolder(inflater.inflate(R.layout.list_add_contact, parent, false))
  }

  override fun getItemCount(): Int {
    return items?.count() ?: 0
  }

  private fun setChecked(view: ImageView) {
    context?.let {
      view.setImageDrawable(it.getDrawable(R.drawable.ic_check))
      view.imageTintList = ColorStateList.valueOf(
          ContextCompat.getColor(it, android.R.color.white))
      view.setBackgroundResource(R.drawable.shape_filled_circle)
      view.setOnClickListener { }
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    items?.get(position).let { item ->

      //set action button
      if (item?.type == UserViewModelType.USER_PHOTOPON) {
        holder.ivAction.setOnClickListener { _ ->
          item.parseUser?.let { user ->
            presenter?.addFriend(user) { success, user ->
              if (success) {
                context?.let { holder.tvContactInfo.text = it.getString(R.string.add_friend_added, user) }
                holder.tvContactInfo.visibility = View.VISIBLE
                setChecked(holder.ivAction)
              }
            }
          }
        }
      } else if (item?.type == UserViewModelType.USER_FRIEND) {
        setChecked(holder.ivAction)
      }

      //Header visibilities
      if (position == 0 || items?.get(position - 1)?.type != item?.type) {
        holder.tvHeader.visibility = View.VISIBLE
      } else {
        holder.tvHeader.visibility = View.GONE
      }

      //header text
      if (item != null) {
        holder.tvHeader.text = getHeaderText(item.type)
      }

      //Full name
      holder.tvFullname.text = item?.fullName

      //Show/hide hint/contact info
      if (item?.extendedInfo.isNullOrBlank()) {
        holder.tvContactInfo.visibility = View.GONE
      } else {
        holder.tvContactInfo.text = item?.extendedInfo
        holder.tvContactInfo.visibility = View.VISIBLE
      }

      //Load profile pic from address book
      item?.localAvatarUri?.let { uri ->
        holder.ivPerson.setPadding(0, 0, 0, 0)
        GlideApp.with(context!!)
            .load(uri)
            .circleCrop()
            .into(holder.ivPerson)
      }

      //Load profile pic from parse
      item?.parseAvatar?.let { file ->
        file.getDataInBackground { data, e ->
          if (e == null && data != null && data.isNotEmpty()) {
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
    }
  }

  private fun getHeaderText(type: UserViewModelType): String? {
    return when (type) {
      UserViewModelType.USER_PHOTOPON -> context?.getString(R.string.add_friend_photopon_users)
      UserViewModelType.USER_FRIEND -> context?.getString(R.string.add_friend_my_friends)
      UserViewModelType.USER_CONTACT -> context?.getString(R.string.add_friend_address_book)
      else -> return null
    }
  }

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvHeader = view.tv_add_contact_header!!
    val tvFullname = view.tv_full_name!!
    val tvContactInfo = view.tv_contact_info!!
    val ivPerson = view.iv_person!!
    val ivAction = view.iv_add_friend!!
  }

}