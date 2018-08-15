package co.photopon.views

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


class NonScrollableViewPager : ViewPager {
  private var drag = 0f

  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  override fun onTouchEvent(event: MotionEvent): Boolean {
    return false
  }

  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    return false
  }

  fun shake() {
    shakePage(8, 0, 100)
    shakePage(-16, 100, 100)
    shakePage(16, 200, 100)
    shakePage(-16, 300, 100)
    shakePage(16, 400, 100)
    shakePage(-16, 500, 100)
    shakePage(16, 600, 100, true)
  }

  private fun shakePage(offset: Int, delay: Long, duration: Long, end: Boolean = false) {
    postDelayed({
      beginFakeDrag()
      drag = 0f
      val density = resources.displayMetrics.density
      // 120 is the number of dps you want the page to move in total
      val animator = ValueAnimator.ofFloat(0f, offset * density)
      animator.addUpdateListener { valueAnimator ->
        val progress = valueAnimator.animatedValue as Float
        fakeDragBy(drag - progress)
        drag = progress
      }
      animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animator: Animator) {}

        override fun onAnimationEnd(animator: Animator) {
          if (end) {
            endFakeDrag()
          }
        }

        override fun onAnimationCancel(animator: Animator) {}

        override fun onAnimationRepeat(animator: Animator) {}
      })
      animator.duration = duration
      animator.start()
    }, delay)
  }
}
