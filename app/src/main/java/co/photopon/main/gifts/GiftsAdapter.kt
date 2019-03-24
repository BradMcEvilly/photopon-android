package co.photopon.main.gifts

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import co.photopon.*
import co.photopon.R
import co.photopon.application.GlideApp
import co.photopon.extensions.formatDate
import co.photopon.main.friends.GiftsPresenter
//import com.parse.Parse
//import com.parse.ParseFile
//import com.parse.ParseObject
import kotlinx.android.synthetic.main.list_gift.view.*


class GiftsAdapter(val context: Context,
                   val presenter: GiftsPresenter,
                   val gifts: List<GiftViewModel>) :
    RecyclerView.Adapter<GiftsAdapter.ViewHolder>() {

  private var TAG: String = "GiftsAdapter"

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(context)
    return ViewHolder(inflater.inflate(R.layout.list_gift, parent, false))
  }

  override fun getItemCount(): Int {
    return gifts.count()
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    Log.i(TAG, "--------------------")
    Log.i(TAG, "onBindViewHolder")
    Log.i(TAG, "--------------------")

    val item = gifts[position]

    Log.i("ParseObj.toGiftViewMod", "==========================")
    Log.i("ParseObj.toGiftViewMod", "==========================")
    Log.i("ParseObj.toGiftViewMod", "==========================")
    Log.i("ParseObj.toGiftViewMod", "==========================")
    Log.i("ParseObj.toGiftViewMod", "==========================")
    Log.i("ParseObj.toGiftViewMod", "item = $item")
    Log.i("ParseObj.toGiftViewMod", "==========================")
    Log.i("ParseObj.toGiftViewMod", "==========================")
    Log.i("ParseObj.toGiftViewMod", "==========================")
    Log.i("ParseObj.toGiftViewMod", "==========================")

    holder.tvTitle.text = item.title
    holder.tvDescription.text = item.description
    holder.tvExpiration.text = context.getString(R.string.gifts_expiration,
        item.expires.formatDate())
    holder.btGet.setOnClickListener { presenter.getCoupon(item) }

    holder.btGive.setOnClickListener { presenter.giveCoupon(item) }

    //Load coupon pic from company parse object

    // if cache is empty for this object, let's make the API call for data
    if (item.couponImage != null) {

      item.couponImage.let { file ->

        Log.i(TAG, "--------------------")
        Log.i("GiftsAdapter", "couponImage.let { file -> file = $file")
        Log.i(TAG, "--------------------")

        file.getDataInBackground { data, e ->

          Log.i(TAG, "--------------------")
          Log.i(TAG, "--------------------")
          Log.i("GiftsAdapter", "couponImage.let { file -> file = $file")
          Log.i(TAG, "--------------------")
          Log.i(TAG, "--------------------")

          Log.i(TAG, "--------------------")
          Log.i(TAG, "file.getDataInBackground { data, e -> e = $e")
          Log.i(TAG, "--------------------")
          //Log.i(TAG, "file.getDataInBackground { data, e -> data = $data")
          Log.i(TAG, "--------------------")

          if (e == null && data != null && data.isNotEmpty()) {

            Log.i(TAG, "------>  if (e == null && data != null && data.isNotEmpty()) {")
            // here we g0\
            holder.ivCoupon.setPadding(0, 0, 0, 0)
            GlideApp.with(context!!)
                    .asBitmap()
                    .load(data)
                    .fitCenter()
                    .circleCrop()
                    .into(holder.ivCoupon)
          }
        }
      }
    }
  }

//    item.couponImage?.let { file ->
//
//      Log.i(TAG, "------> item.couponImage?.let { file -> file = $file")
//
//      file.getDataInBackground { data, e ->
//
//        Log.i(TAG, "--------------------")
//        Log.i(TAG, "file.getDataInBackground { data, e -> e = $e")
//        Log.i(TAG, "--------------------")
//        Log.i(TAG, "file.getDataInBackground { data, e -> data = $data")
//        Log.i(TAG, "--------------------")
//
//        if (e == null && data != null && data.isNotEmpty()) {
//
//          Log.i(TAG, "------>  if (e == null && data != null && data.isNotEmpty()) {")
//          // here we g0\
//          holder.ivCoupon.setPadding(0, 0, 0, 0)
//          GlideApp.with(context!!)
//                  .asBitmap()
//                  .load(data)
//                  .fitCenter()
//                  .circleCrop()
//                  .into(holder.ivCoupon)
//        }
//      }
//    }

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivCoupon = view.iv_coupon!!
    val tvTitle = view.tv_coupon_title!!
    val tvDescription = view.tv_coupon_description!!
    val tvExpiration = view.tv_coupon_expiration!!
    val btGet = view.bt_get!!
    val btGive = view.bt_give!!
  }

}