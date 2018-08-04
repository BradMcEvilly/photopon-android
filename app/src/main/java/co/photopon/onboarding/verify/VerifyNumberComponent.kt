package co.photopon.onboarding.verify

import co.photopon.application.scopes.ActivityScope
import dagger.Subcomponent

/**
 * Created by tyler on 12/14/17.
 */
@ActivityScope
@Subcomponent(modules = [(VerifyNumberModule::class)])
interface VerifyNumberComponent {

  fun inject(validateNumberActivity: VerifyNumberActivity)

}
