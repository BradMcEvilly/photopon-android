package co.photopon.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.photopon.R
import co.photopon.application.PhotoponActivity
import co.photopon.friends.UserViewModel
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : PhotoponActivity() {
  override fun isFullscreen(): Boolean {
    return true
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    UXCam.startWithKey("1p78vfc6zdrv4cx")

    setContentView(R.layout.activity_chat)

    val friend = intent.getParcelableExtra<UserViewModel>("friend")

    tv_title.text = friend.fullName
    ib_back.setOnClickListener { finish() }
  }

  companion object {
    fun getIntent(context: Context, friend: UserViewModel): Intent {
      val intent = Intent(context, ChatActivity::class.java)
      intent.putExtra("friend", friend)

      return intent
    }
  }

}