package co.photopon.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.photopon.R
import co.photopon.main.MainActivity
import co.photopon.onboarding.OnboardingActivity
import com.parse.ParseUser
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    Timer().schedule(timerTask { nextActivity() }, 500)
  }

  private fun nextActivity() {
    if (ParseUser.getCurrentUser() == null) {
      startActivity(OnboardingActivity.getIntent(this))
    } else {
      startActivity(MainActivity.getIntent(this))
    }
    finish()
  }

}
