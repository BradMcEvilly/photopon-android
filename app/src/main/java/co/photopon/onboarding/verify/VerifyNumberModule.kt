package co.photopon.onboarding.verify

import android.content.Context
import co.photopon.managers.PreferencesManager
import co.photopon.managers.SmsManager
import co.photopon.managers.UserManager
import dagger.Module
import dagger.Provides

/**
 * Created by tyler on 12/14/17.
 */

@Module
class VerifyNumberModule(val view: VerifyNumberView) {

  @Provides
  fun provideView(): VerifyNumberView = view

  @Provides
  fun providePresenter(view: VerifyNumberView,
                       context: Context,
                       smsManager: SmsManager,
                       userManager: UserManager,
                       preferencesManager: PreferencesManager): VerifyNumberPresenter =
      VerifyNumberPresenterImpl(view, context, smsManager, userManager, preferencesManager)

}
