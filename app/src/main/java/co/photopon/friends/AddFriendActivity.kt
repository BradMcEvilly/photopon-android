package co.photopon.friends

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import co.photopon.R
import co.photopon.application.PhotoponActivity
import co.photopon.extensions.mainApplication
import kotlinx.android.synthetic.main.activity_add_friend.*
import javax.inject.Inject

class AddFriendActivity : PhotoponActivity(), AddFriendView {

  @Inject
  lateinit var presenter: AddFriendPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_friend)

    mainApplication.appComponent().plus(AddFriendModule(this)).inject(this)

    checkPermission()

    ib_back.setOnClickListener { onBackPressed() }
    ib_clear_search.setOnClickListener { clearList() }
    et_add_friend.addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(p0: Editable?) = Unit

      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

      override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
        text?.let {
          when (it.length) {
            0 -> clearList()
            else -> presenter.search(it.toString())
          }
        }
      }
    })
  }

  override fun clearList() {
    et_add_friend.text.clear()
    val emptyList = arrayListOf<UserViewModel>()
    rv_friends.adapter = AddFriendAdapter(this, presenter, emptyList, emptyList, emptyList)
  }

  override fun isFullscreen(): Boolean {
    return true
  }

  override fun onBackPressed() {
    finish()
    slideDown()
  }

  override fun displayUsers(users: List<UserViewModel>, friends: List<UserViewModel>,
                            contacts: List<UserViewModel>) {
    val adapter = AddFriendAdapter(this, presenter, users, friends, contacts)
    val linearLayoutManager = LinearLayoutManager(this)
    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
    rv_friends.layoutManager = linearLayoutManager
    rv_friends.adapter = adapter
  }

  private fun checkPermission() {
    val contactsPermission = ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_CONTACTS)

    if (contactsPermission != PackageManager.PERMISSION_GRANTED) {
      requestPermission(REQUEST_CONTACTS)
    }
  }

  companion object {
    fun getIntent(context: Context): Intent {
      return Intent(context, AddFriendActivity::class.java)
    }
  }
}