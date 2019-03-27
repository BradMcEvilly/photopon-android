package co.photopon.main.make

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import co.photopon.application.GlideApp
import co.photopon.main.gifts.GiftViewModel
//import com.twilio.client.impl.TwilioImpl.context
import kotlinx.android.synthetic.main.item_coupon.view.*

class MakeViewPagerAdapter(context: Context,
                           private val mItems: ArrayList<GiftViewModel>) : PagerAdapter() {
  private val discarded = ArrayList<View>()
  private val binded = SparseArray<View>()
  private val inflator = LayoutInflater.from(context)
  private val TAG: String = "MakeViewPagerAdapter"

  override fun getCount(): Int {
    return mItems.count()
  }

  override fun isViewFromObject(v: View, obj: Any): Boolean {
    return v === binded.get(mItems.indexOf(obj))
  }

  override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
    val view = binded.get(position)
    if (view != null) {
      discarded.add(view)
      binded.remove(position)
      container.removeView(view)
    }
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val child = if (discarded.isEmpty()) {
      inflator.inflate(R.layout.item_coupon, container, false)
    } else {
      discarded.removeAt(0)
    }

    val data = mItems[position]

    child.tv_coupon_title.text = data.title
    child.tv_coupon_description.text = data.description

    if (data.couponImage != null) {

      Log.i(TAG, "-----------------------")
      Log.i(TAG, "if (data.couponImage != null) {")
      Log.i(TAG, "-----------------------")

      data.couponImage.let { file ->

        Log.i(TAG, "--------------------")
        Log.i(TAG, "couponImage.let { file -> file = $file")
        Log.i(TAG, "--------------------")

        file.getDataInBackground { fileData, e ->

          Log.i(TAG, "--------------------")
          Log.i(TAG, "--------------------")
          Log.i(TAG, "couponImage.let { file -> file = $file")
          Log.i(TAG, "--------------------")
          Log.i(TAG, "--------------------")

          Log.i(TAG, "--------------------")
          Log.i(TAG, "file.getDataInBackground { data, e -> e = $e")
          Log.i(TAG, "--------------------")
          //Log.i(TAG, "file.getDataInBackground { data, e -> data = $data")
          Log.i(TAG, "--------------------")

          if (e == null && fileData != null && fileData.isNotEmpty()) {

            Log.i(TAG, "------>  if (e == null && data != null && data.isNotEmpty()) {")
            // here we g0\
            child.iv_coupon.setPadding(0, 0, 0, 0)

            Log.i(TAG, "-----------------------")
            Log.i(TAG, "child.iv_coupon.setpadding...")
            Log.i(TAG, "-----------------------")

            GlideApp.with(child)
                    .asBitmap()
                    .load(fileData)
                    .fitCenter()
                    .circleCrop()
                    .into(child.iv_coupon)

            Log.i(TAG, "-----------------------")
            Log.i(TAG, "GlideApp.with(context)...)")
            Log.i(TAG, "-----------------------")

          }

          binded.append(position, child)

          Log.i(TAG, "-----------------------")
          Log.i(TAG, "binded.append(position, child)")
          Log.i(TAG, "-----------------------")

          container.addView(child, 0)

          Log.i(TAG, "-----------------------")
          Log.i(TAG, "container.addView(child, 0)")
          Log.i(TAG, "-----------------------")

        }
      }
    } else {

      Log.i(TAG, "-----------------------")
      Log.i(TAG, "if (data.couponImage != null) {   ELSE")
      Log.i(TAG, "-----------------------")

      binded.append(position, child)

      Log.i(TAG, "-----------------------")
      Log.i(TAG, "binded.append(position, child)")
      Log.i(TAG, "-----------------------")

      container.addView(child, 0)

      Log.i(TAG, "-----------------------")
      Log.i(TAG, "container.addView(child, 0)")
      Log.i(TAG, "-----------------------")
    }
    return data
  }

  fun add(item: GiftViewModel) {
    mItems.add(item)
  }
}
