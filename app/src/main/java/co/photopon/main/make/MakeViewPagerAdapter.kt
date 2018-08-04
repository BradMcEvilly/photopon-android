package co.photopon.main.make

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.photopon.R
import co.photopon.main.gifts.GiftViewModel
import kotlinx.android.synthetic.main.item_coupon.view.*

class MakeViewPagerAdapter(context: Context,
                           private val mItems: ArrayList<GiftViewModel>) : PagerAdapter() {
  private val discarded = ArrayList<View>()
  private val binded = SparseArray<View>()
  private val inflator = LayoutInflater.from(context)

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

    binded.append(position, child)
    container.addView(child, 0)
    return data
  }

  fun add(item: GiftViewModel) {
    mItems.add(item)
  }
}
