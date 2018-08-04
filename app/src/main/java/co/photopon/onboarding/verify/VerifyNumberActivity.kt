package co.photopon.onboarding.verify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.photopon.R
import co.photopon.application.PhotoponActivity
import co.photopon.extensions.mainApplication
import co.photopon.onboarding.verify.VerifyPagerAdapter.Companion.PAGE_CHOOSE_USERNAME
import co.photopon.onboarding.verify.VerifyPagerAdapter.Companion.PAGE_SENDING_CODE
import co.photopon.onboarding.verify.VerifyPagerAdapter.Companion.PAGE_VERIFY_CODE
import co.photopon.onboarding.verify.VerifyPagerAdapter.Companion.PAGE_VERIFY_PHONE
import co.photopon.onboarding.verify.VerifyPagerAdapter.Companion.PAGE_WELCOME_BACK
import kotlinx.android.synthetic.main.activity_validate_phone.*
import javax.inject.Inject


class VerifyNumberActivity : PhotoponActivity(), VerifyNumberView,
    VerifyPhoneFragment.OnVerifyNumberListener, VerifyCodeFragment.OnVerifyCodeListener,
    VerifyUsernameFragment.OnGetStartedListener, VerifyWelcomeBackFragment.OnGetStartedListener {

  @Inject
  lateinit var presenter: VerifyNumberPresenter

  private lateinit var pagerAdapter: VerifyPagerAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_validate_phone)

    mainApplication.appComponent().plus(VerifyNumberModule(this)).inject(this)

    pagerAdapter = VerifyPagerAdapter(supportFragmentManager)

    view_pager.adapter = pagerAdapter
    view_pager.pageMargin = 100
  }

  override fun isFullscreen(): Boolean {
    return false
  }

  override fun displayInvalidError() {
    showOkDialog(R.string.verify_phone, R.string.verify_invalid)
  }

  override fun displayUsernameError() {
    showOkDialog(R.string.verify_username, R.string.verify_invalid)
  }

  override fun displayInvalidInput() {
    view_pager.shake()
  }

  override fun navigateToVerifyPhone() {
    view_pager.currentItem = PAGE_VERIFY_PHONE
  }

  override fun navigateToSendingCode() {
    view_pager.currentItem = PAGE_SENDING_CODE
  }

  override fun navigateToVerifyCode() {
    runOnUiThread { view_pager.currentItem = PAGE_VERIFY_CODE }
  }

  override fun navigateToWelcomeBack(name: String) {
    pagerAdapter.setWelcomeBackName(name)
    view_pager.currentItem = PAGE_WELCOME_BACK
  }

  override fun navigateToChooseUsername() {
    view_pager.currentItem = PAGE_CHOOSE_USERNAME
  }

  override fun navigateToNextActivity() {
    setResult(RESULT_SIGNED_UP)
    finish()
  }

  override fun onVerifyNumberClicked(number: String) {
    presenter.verifyNumber(number)
  }

  override fun onVerifyCodeClicked(code: String) {
    presenter.verifyCode(code)
  }

  override fun onGetStartedClicked(userName: String) {
    presenter.chooseUsername(userName)
  }

  override fun onGetStartedClicked() {
    dropOut()
  }

  override fun onLaterClicked() {
    dropOut()
  }

  override fun completed() {
    dropOut()
  }

  override fun onCancelClicked() {
    dropOut()
  }

  override fun onResendCodeClicked() {
    presenter.resendCode()
  }

  companion object {
    var RESULT_SIGNED_UP = 9238

    fun getIntent(context: Context): Intent {
      return Intent(context, VerifyNumberActivity::class.java)
    }
  }
}
