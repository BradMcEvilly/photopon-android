package co.photopon.main.friends

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import co.photopon.extensions.formatDate
import co.photopon.main.gifts.GiftViewModel
import kotlinx.android.synthetic.main.list_gift.view.*


class GiftsAdapter(val context: Context,
                   val presenter: GiftsPresenter,
                   val gifts: List<GiftViewModel>) :
    RecyclerView.Adapter<GiftsAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(context)
    return ViewHolder(inflater.inflate(R.layout.list_gift, parent, false))
  }

  override fun getItemCount(): Int {
    return gifts.count()
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = gifts[position]

    holder.tvTitle.text = item.title
    holder.tvDescription.text = item.description
    holder.tvExpiration.text = context.getString(R.string.gifts_expiration,
        item.expires.formatDate())
    holder.btGet.setOnClickListener { presenter.getCoupon(item) }
    holder.btGive.setOnClickListener { presenter.giveCoupon(item) }

//    //Load profile pic from parse
//    item.parseAvatar?.let { file ->
//      file.getDataInBackground { data, e ->
//        if (e == null && data != null && data.isNotEmpty()) {
//          holder.ivPerson.setPadding(0, 0, 0, 0)
//          GlideApp.with(context)
//              .asBitmap()
//              .load(data)
//              .fitCenter()
//              .circleCrop()
//              .into(holder.ivPerson)
//        }
//      }
//    }
  }

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivCoupon = view.iv_coupon!!
    val tvTitle = view.tv_coupon_title!!
    val tvDescription = view.tv_coupon_description!!
    val tvExpiration = view.tv_coupon_expiration!!
    val btGet = view.bt_get!!
    val btGive = view.bt_give!!
  }

}