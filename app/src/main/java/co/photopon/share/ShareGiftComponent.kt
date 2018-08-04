package co.photopon.friends

import co.photopon.application.scopes.ActivityScope
import co.photopon.share.ShareGiftActivity
import dagger.Subcomponent

/**
 * Created by tyler on 12/14/17.
 */
@ActivityScope
@Subcomponent(modules = [(ShareGiftModule::class)])
interface ShareGiftComponent {

  fun inject(shareGiftActivity: ShareGiftActivity)

}
