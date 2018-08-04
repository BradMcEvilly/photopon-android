package co.photopon.onboarding.verify

import android.content.Context
import co.photopon.managers.PreferencesManager
import co.photopon.managers.SmsManager
import co.photopon.managers.UserManager

/**
 * Created by tyler on 12/14/17.
 */

interface VerifyNumberView {
  fun navigateToVerifyPhone()

  fun navigateToSendingCode()

  fun navigateToVerifyCode()

  fun navigateToWelcomeBack(name: String)

  fun navigateToChooseUsername()

  fun navigateToNextActivity()

  fun displayInvalidError()

  fun displayGenericError()

  fun displayInvalidInput()

  fun displayUsernameError()

  fun completed()
}

interface VerifyNumberPresenter {
  val view: VerifyNumberView

  val context: Context

  val smsManager: SmsManager

  val userManager: UserManager

  val preferencesManager: PreferencesManager

  fun verifyNumber(number: String)

  fun verifyCode(code: String)

  fun chooseUsername(userName: String)

  fun resendCode()
}
