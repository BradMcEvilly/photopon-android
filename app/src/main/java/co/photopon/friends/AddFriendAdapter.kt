package co.photopon.friends

import android.content.Context
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
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
  private val TAG: String = "AddFriendAdapter"

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

    Log.i(TAG, "--------------------")
    Log.i(TAG, "onCreateViewHolder")
    Log.i(TAG, "--------------------")

    val inflater = LayoutInflater.from(context)

    Log.i(TAG, "--------------------")
    Log.i(TAG, "onCreateViewHolder  val inflater = LayoutInflater.from(context)")
    Log.i(TAG, "--------------------")

    return ViewHolder(inflater.inflate(R.layout.list_add_contact, parent, false))
  }

  override fun getItemCount(): Int {

    Log.i(TAG, "--------------------")
    Log.i(TAG, "getItemCount")
    Log.i(TAG, "--------------------")

    return items?.count() ?: 0
  }

  private fun setChecked(view: ImageView) {

    Log.i(TAG, "--------------------")
    Log.i(TAG, "setChecked")
    Log.i(TAG, "--------------------")

    context?.let {

      Log.i(TAG, "--------------------")
      Log.i(TAG, "setChecked  context?.let {")
      Log.i(TAG, "--------------------")

      view.setImageDrawable(it.getDrawable(R.drawable.ic_check))
      view.imageTintList = ColorStateList.valueOf(
          ContextCompat.getColor(it, android.R.color.white))
      view.setBackgroundResource(R.drawable.shape_filled_circle)
      view.setOnClickListener { }
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    Log.i(TAG, "--------------------")
    Log.i(TAG, "onBindViewHolder() — holder = $holder")
    Log.i(TAG, "onBindViewHolder() — position = $position")
    Log.i(TAG, "--------------------")


    items?.get(position).let { item ->

      Log.i(TAG, "--------------------")
      Log.i(TAG, "items?.get(position).let { item -> item = $item")
      Log.i(TAG, "--------------------")

      //set action button
      if (item?.type == UserViewModelType.USER_PHOTOPON) {

        Log.i(TAG, "--------------------")
        Log.i(TAG, "if (item?.type == UserViewModelType.USER_PHOTOPON) {")
        Log.i(TAG, "--------------------")

        holder.ivAction.setOnClickListener { _ ->

          Log.i(TAG, "--------------------")
          Log.i(TAG, "holder.ivAction.setOnClickListener { _ ->")
          Log.i(TAG, "--------------------")

          item.parseUser?.let { user ->

            Log.i(TAG, "--------------------")
            Log.i(TAG, "item.parseUser?.let { user -> user = $user")
            Log.i(TAG, "--------------------")

            presenter?.addFriend(user) { success, user ->

              Log.i(TAG, "--------------------")
              Log.i(TAG, "presenter?.addFriend(user) { success, user ->")
              Log.i(TAG, "--------------------")


              if (success) {

                Log.i(TAG, "--------------------")
                Log.i(TAG, "if (success) { success = $success")
                Log.i(TAG, "--------------------")

                context?.let { holder.tvContactInfo.text = it.getString(R.string.add_friend_added, user) }
                holder.tvContactInfo.visibility = View.VISIBLE
                setChecked(holder.ivAction)
              }
            }
          }
        }
      } else if (item?.type == UserViewModelType.USER_FRIEND) {

        Log.i(TAG, "--------------------")
        Log.i(TAG, "} else if (item?.type == UserViewModelType.USER_FRIEND) {")
        Log.i(TAG, "--------------------")

        setChecked(holder.ivAction)
      }

      //Header visibilities
      if (position == 0 || items?.get(position - 1)?.type != item?.type) {

        Log.i(TAG, "--------------------")
        Log.i(TAG, "if (position == 0 || items?.get(position - 1)?.type != item?.type) {")
        Log.i(TAG, "--------------------")

        holder.tvHeader.visibility = View.VISIBLE
      } else {

        Log.i(TAG, "--------------------")
        Log.i(TAG, "if (position == 0 || items?.get(position - 1)?.type != item?.type) { ... } else {")
        Log.i(TAG, "--------------------")

        holder.tvHeader.visibility = View.GONE
      }

      //header text
      if (item != null) {

        Log.i(TAG, "--------------------")
        Log.i(TAG, "presenter?.addFriend(user) { success, user ->")
        Log.i(TAG, "--------------------")

        holder.tvHeader.text = getHeaderText(item.type)
      }

      //Full name
      holder.tvFullname.text = item?.fullName

      //Show/hide hint/contact info
      if (item?.extendedInfo.isNullOrBlank()) {

        Log.i(TAG, "--------------------")
        Log.i(TAG, "if (item?.extendedInfo.isNullOrBlank()) {")
        Log.i(TAG, "--------------------")

        holder.tvContactInfo.visibility = View.GONE
      } else {
        holder.tvContactInfo.text = item?.extendedInfo
        holder.tvContactInfo.visibility = View.VISIBLE
      }

      //Load profile pic from address book
      item?.localAvatarUri?.let { uri ->

        Log.i(TAG, "--------------------")
        Log.i(TAG, "item?.localAvatarUri?.let { uri -> uri = $uri")
        Log.i(TAG, "--------------------")

        holder.ivPerson.setPadding(0, 0, 0, 0)
        GlideApp.with(context!!)
            .load(uri)
            .circleCrop()
            .into(holder.ivPerson)
      }


      //Load profile pic from parse
      item?.parseAvatar?.let { file ->

        Log.i(TAG, "------> item?.parseAvatar?.let { file -> file = $file")

        file.getDataInBackground { data, e ->

          Log.i(TAG, "--------------------")
          Log.i(TAG, "file.getDataInBackground { data, e -> e = $e")
          Log.i(TAG, "--------------------")
          Log.i(TAG, "file.getDataInBackground { data, e -> data = $data")
          Log.i(TAG, "--------------------")


          if (e == null && data != null && data.isNotEmpty()) {

            Log.i(TAG, "------>  if (e == null && data != null && data.isNotEmpty()) {")

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
    Log.i(TAG, "getHeaderText")
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