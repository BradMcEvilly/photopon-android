package co.photopon.main.draw

import co.photopon.application.scopes.ActivityScope
import dagger.Subcomponent

/**
 * Created by tyler on 12/14/17.
 */
@ActivityScope
@Subcomponent(modules = [(DrawCouponModule::class)])
interface DrawCouponComponent {

  fun inject(drawCouponActivity: DrawCouponActivity)

}
