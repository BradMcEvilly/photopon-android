package co.photopon.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.photopon.R
import co.photopon.application.GlideApp
import co.photopon.application.PhotoponActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : PhotoponActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)

    GlideApp.with(this)
        .load("http://goo.gl/gEgYUd")
        .centerInside()
        .circleCrop()
        .into(iv_avatar)

    ib_close.setOnClickListener { onBackPressed() }
  }

  override fun isFullscreen(): Boolean {
    return true
  }

  override fun onBackPressed() {
    finish()
    slideDown()
  }

  companion object {
    fun getIntent(context: Context): Intent {
      return Intent(context, SettingsActivity::class.java)
    }
  }
}