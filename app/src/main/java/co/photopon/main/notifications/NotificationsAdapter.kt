package co.photopon.main.friends

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import co.photopon.R
import co.photopon.application.GlideApp
import co.photopon.extensions.formatDate
import co.photopon.friends.UserViewModelType
import co.photopon.friends.toUserViewModel
import co.photopon.main.gifts.NotificationViewModel
import co.photopon.main.gifts.NotificationViewModelType
import co.photopon.main.gifts.toGiftViewModel
import com.parse.ParseObject
import kotlinx.android.synthetic.main.list_notification_addwallet.view.*
import kotlinx.android.synthetic.main.list_notification_friend.view.*
import kotlinx.android.synthetic.main.list_notification_message.view.*
import kotlinx.android.synthetic.main.list_notification_photopon.view.*
import kotlinx.android.synthetic.main.list_notification_redeemed.view.*
import kotlinx.android.synthetic.main.list_notification_unlockedcoupon.view.*


class NotificationsAdapter(val context: Context,
                           val presenter: NotificationsPresenter,
                           val notifications: List<NotificationViewModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private val TAG: String = "NotificationsAdapter"

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val inflater = LayoutInflater.from(context)
    return when (viewType) {
      VIEW_TYPE_PHOTOPON -> PhotoponViewHolder(
          inflater.inflate(R.layout.list_notification_photopon, parent, false))
      VIEW_TYPE_FRIEND -> FriendViewHolder(
          inflater.inflate(R.layout.list_notification_friend, parent, false))
      VIEW_TYPE_UNLOCKEDCOUPON -> UnlockedCouponViewHolder(
              inflater.inflate(R.layout.list_notification_unlockedcoupon, parent, false))
      VIEW_TYPE_REDEEMED -> RedeemedViewHolder(
              inflater.inflate(R.layout.list_notification_redeemed, parent, false))
      VIEW_TYPE_ADDWALLET -> AddWalletViewHolder(
              inflater.inflate(R.layout.list_notification_addwallet, parent, false))
      VIEW_TYPE_MESSAGE -> MessageViewHolder(
          inflater.inflate(R.layout.list_notification_message, parent, false))
      else -> throw RuntimeException("Invalid View Type")
    }
  }

  override fun getItemCount(): Int {
    return notifications.count()
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = notifications[position]

    when (holder.itemViewType) {
      VIEW_TYPE_PHOTOPON -> populatePhotopon(holder, item)
      VIEW_TYPE_FRIEND -> populateFriend(holder, item)
      VIEW_TYPE_UNLOCKEDCOUPON -> populateUnlockedCoupon(holder, item)
      VIEW_TYPE_REDEEMED -> populateRedeemed(holder, item)
      VIEW_TYPE_ADDWALLET -> populateAddWallet(holder, item)
      VIEW_TYPE_MESSAGE -> populateMessage(holder, item)
    }
  }

  override fun getItemViewType(position: Int): Int {
    return when (notifications[position].type) {
      NotificationViewModelType.FRIEND -> VIEW_TYPE_FRIEND
      NotificationViewModelType.PHOTOPON -> VIEW_TYPE_PHOTOPON
      NotificationViewModelType.UNLOCKEDCOUPON -> VIEW_TYPE_UNLOCKEDCOUPON
      NotificationViewModelType.REDEEMED -> VIEW_TYPE_REDEEMED
      NotificationViewModelType.ADDWALLET -> VIEW_TYPE_ADDWALLET
      NotificationViewModelType.MESSAGE -> VIEW_TYPE_MESSAGE
    }
  }

  private fun populatePhotopon(holder: RecyclerView.ViewHolder, item: NotificationViewModel) {

    Log.i(TAG, "--------------------")
    Log.i(TAG, "populatePhotopon")
    Log.i(TAG, "--------------------")

    holder as PhotoponViewHolder

    val coupon = (item.assocPhotopon?.get("coupon") as? ParseObject)!!.toGiftViewModel()
    val user = (item.assocUser as? ParseObject)!!.toUserViewModel(UserViewModelType.USER_PHOTOPON)
    //val company = (item.assocPhotopon?.get("company") as? ParseObject)!!.toCompanyViewModel()

    holder.tvTitle.text = coupon.title
    holder.tvDescription.text = coupon.description
    holder.tvExpiration.text = coupon.expires.formatDate()
    holder.tvPersonTitle.text = user.fullName

    // holder.btGet.setOnClickListener { presenter.getCoupon(item) }
    // holder.btRedeem.setOnClickListener { presenter.getCoupon(item) }


    coupon.couponImage?.let { file ->

      Log.i(TAG, "--------------------")
      Log.i(TAG, "populatePhotopon :: coupon.couponImage?.let { file -> ")
      Log.i(TAG, "--------------------")

      file.getDataInBackground { data, e ->

        Log.i(TAG, "--------------------")
        Log.i(TAG, "populatePhotopon :: [coupon.couponImage]file.getDataInBackground  e = $e")
        Log.i(TAG, "--------------------")

        if (e  == null && data != null && data.isNotEmpty()) {

          Log.i(TAG, "--------------------")
          Log.i(TAG, "populatePhotopon   couponImage ...if (e == null && data != null && data.isNotEmpty()) { ")
          Log.i(TAG, "--------------------")

          holder.ivCoupon.setPadding(0, 0, 0, 0)

          GlideApp.with(context)
                  .asBitmap()
                  .load(data)
                  .fitCenter()
                  .circleCrop()
                  .into(holder.ivCoupon)
        }
      }
    }


    //Load profile pic from parse todo extension
    user.parseAvatar?.let { file ->

      Log.i(TAG, "--------------------")
      Log.i(TAG, "populatePhotopon :: user.parseAvatar?.let { file -> ")
      Log.i(TAG, "--------------------")

      file.getDataInBackground { data, e ->

        Log.i(TAG, "--------------------")
        Log.i(TAG, "populatePhotopon :: file.getDataInBackground  e = $e")
        Log.i(TAG, "--------------------")

        if (e == null && data != null && data.isNotEmpty()) {

          Log.i(TAG, "--------------------")
          Log.i(TAG, "populatePhotopon    ...if (e == null && data != null && data.isNotEmpty()) { ")
          Log.i(TAG, "--------------------")

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

  private fun populateUnlockedCoupon(holder: RecyclerView.ViewHolder, item: NotificationViewModel) {

    Log.i(TAG, "--------------------")
    Log.i(TAG, "populateUnlockedCoupon")
    Log.i(TAG, "--------------------")

    holder as UnlockedCouponViewHolder
    val user = (item.assocUser as? ParseObject)!!.toUserViewModel(UserViewModelType.USER_PHOTOPON)

    holder.tvTitle.text = context.getString(R.string.notifications_unlocked_coupon, user.fullName)

    //Load profile pic from parse todo extension
    user.parseAvatar?.let { file ->
      file.getDataInBackground { data, e ->
        if (e == null && data != null && data.isNotEmpty()) {
          holder.ivPerson.setPadding(0, 0, 0, 0)
          GlideApp.with(context) //todo extension
                  .asBitmap()
                  .load(data)
                  .fitCenter()
                  .circleCrop()
                  .into(holder.ivPerson)
        }
      }
    }
  }

  private fun populateRedeemed(holder: RecyclerView.ViewHolder, item: NotificationViewModel) {

    Log.i(TAG, "--------------------")
    Log.i(TAG, "populateRedeemed")
    Log.i(TAG, "--------------------")

    holder as RedeemedViewHolder
    val user = (item.assocUser as? ParseObject)!!.toUserViewModel(UserViewModelType.USER_PHOTOPON)

    holder.tvTitle.text = context.getString(R.string.notifications_redeemed_photopon, user.fullName)

    //Load profile pic from parse todo extension
    user.parseAvatar?.let { file ->
      file.getDataInBackground { data, e ->
        if (e == null && data != null && data.isNotEmpty()) {
          holder.ivPerson.setPadding(0, 0, 0, 0)
          GlideApp.with(context) //todo extension
                  .asBitmap()
                  .load(data)
                  .fitCenter()
                  .circleCrop()
                  .into(holder.ivPerson)
        }
      }
    }
  }

  private fun populateAddWallet(holder: RecyclerView.ViewHolder, item: NotificationViewModel) {

    Log.i(TAG, "--------------------")
    Log.i(TAG, "populateAddWallet")
    Log.i(TAG, "--------------------")

    holder as AddWalletViewHolder

    val user = (item.assocUser as? ParseObject)!!.toUserViewModel(UserViewModelType.USER_PHOTOPON)

    holder.tvTitle.text = context.getString(R.string.notifications_saved_photopon, user.fullName)

    //Load profile pic from parse todo extension
    user.parseAvatar?.let { file ->
      file.getDataInBackground { data, e ->
        if (e == null && data != null && data.isNotEmpty()) {
          holder.ivPerson.setPadding(0, 0, 0, 0)
          GlideApp.with(context) //todo extension
                  .asBitmap()
                  .load(data)
                  .fitCenter()
                  .circleCrop()
                  .into(holder.ivPerson)
        }
      }
    }
  }



  private fun populateMessage(holder: RecyclerView.ViewHolder, item: NotificationViewModel) {

    Log.i(TAG, "--------------------")
    Log.i(TAG, "populateMessage")
    Log.i(TAG, "--------------------")

    holder as MessageViewHolder

    val user = (item.assocUser as? ParseObject)!!.toUserViewModel(UserViewModelType.USER_PHOTOPON)

    holder.tvTitle.text = context.getString(R.string.notifications_messaged_you, user.fullName)

    holder.layout.setOnClickListener { presenter.onChatClicked(user) }

    //Load profile pic from parse todo extension
    user.parseAvatar?.let { file ->
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

  private fun populateFriend(holder: RecyclerView.ViewHolder, item: NotificationViewModel) {

    Log.i(TAG, "--------------------")
    Log.i(TAG, "populateFriend")
    Log.i(TAG, "--------------------")

    holder as FriendViewHolder

    val user = (item.assocUser as? ParseObject)!!.toUserViewModel(UserViewModelType.USER_PHOTOPON)

    holder.tvTitle.text = context.getString(R.string.notifications_added_you, user.fullName)
    holder.tvDescription.text = context.getString(R.string.notifications_add_back)

    //Load profile pic from parse todo extension
    user.parseAvatar?.let { file ->
      file.getDataInBackground { data, e ->
        if (e == null && data != null && data.isNotEmpty()) {
          holder.ivPerson.setPadding(0, 0, 0, 0)
          GlideApp.with(context) //todo extension
              .asBitmap()
              .load(data)
              .fitCenter()
              .circleCrop()
              .into(holder.ivPerson)
        }
      }
    }
  }

  class AddWalletViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivPerson = view.iv_person_addwallet!!
    val tvTitle = view.tv_title_addwallet!!
  }

  class RedeemedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivPerson = view.iv_person_redeemed!!
    val tvTitle = view.tv_title_redeemed!!
  }

  class UnlockedCouponViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivPerson = view.iv_person_unlockedcoupon!!
    val tvTitle = view.tv_title_unlockedcoupon!!
  }

  class PhotoponViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val ivPerson = view.iv_person_photopon!!
    val ivCoupon= view.iv_coupon!!
    val tvTitle = view.tv_coupon_title!!
    val tvDescription = view.tv_coupon_description!!
    val tvExpiration = view.tv_coupon_expiration!!
    val tvPersonTitle = view.tv_person_title!!
  }

  class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivPerson = view.iv_person!!
    val tvTitle = view.tv_title!!
    val tvDescription = view.tv_description!!
  }

  class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivPerson = view.iv_message_person!!
    val tvTitle = view.tv_message_title!!
    val layout = view.layout_notification!!
  }

  companion object {
    const val VIEW_TYPE_FRIEND = 1235
    const val VIEW_TYPE_PHOTOPON = 2315
    const val VIEW_TYPE_MESSAGE = 3962
    const val VIEW_TYPE_ADDWALLET = 4393
    const val VIEW_TYPE_UNLOCKEDCOUPON = 4561
    const val VIEW_TYPE_REDEEMED = 5647
    const val VIEW_TYPE_WELCOME_MESSAGE = 6543
    const val VIEW_TYPE_VERIFICATION_MESSAGE = 7192

  }

}