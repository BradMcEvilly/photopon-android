package co.photopon.onboarding.verify

import android.content.Context
import co.photopon.extensions.nextInt
import co.photopon.extensions.numbersOnly
import co.photopon.managers.PreferencesManager
import co.photopon.managers.SmsManager
import co.photopon.managers.UserManager
import com.parse.LogInCallback
import com.parse.SaveCallback
import com.parse.SignUpCallback
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Created by tyler on 12/14/17.
 */

class VerifyNumberPresenterImpl(override val view: VerifyNumberView,
                                override val context: Context,
                                override val smsManager: SmsManager,
                                override val userManager: UserManager,
                                override val preferencesManager: PreferencesManager) :
    VerifyNumberPresenter {

  private val TAG = javaClass.simpleName

  private var code: String? = null
  private var number: String? = null

  override fun verifyNumber(number: String) {
    when (number.isBlank()) {
      true -> view.displayInvalidError()
      false -> sendSMS(number)
    }
  }

  override fun verifyCode(code: String) {
    if (this.code == code) {
      val number = number?.numbersOnly() ?: ""
      userManager.queryUser("phone", number, { name ->
        userManager.signInUser(number, LogInCallback { _, e ->
          if (e != null) {
            view.displayGenericError()
          } else {
            view.navigateToWelcomeBack(name)
          }
        })
      }, {
        view.navigateToChooseUsername()
      }, {
        view.displayGenericError()
      })
    } else {
      view.displayInvalidInput()
    }
  }

  override fun chooseUsername(userName: String) {
    if (userName.isBlank()) {
      view.displayInvalidInput()
    } else {
      userManager.queryUser("username", userName, { _ ->
        view.displayUsernameError()
      }, {
        registerUser(userName)
      }, {
        view.displayGenericError()
      })
    }
  }

  override fun resendCode() {
    sendSMS(number ?: "")
  }

  private fun registerUser(userName: String) {
    userManager.registerUser(userName, number ?: "", SignUpCallback {
      if (it == null) {
        view.completed()
      } else {
        view.displayGenericError()
      }
    })
  }

  private fun sendSMS(number: String) {
    view.navigateToSendingCode()

    val code = Random().nextInt(100000..1000000).toString()

    this.code = code
    this.number = number.numbersOnly()

    smsManager.sendCode(this.number ?: "", code, SaveCallback { exception ->
      if (exception == null) {
        Timer().schedule(timerTask { view.navigateToVerifyCode() }, 2000)
      } else {
        view.displayGenericError()
        view.navigateToVerifyPhone()
      }
    })
  }

}