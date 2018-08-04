package co.photopon.friends

import co.photopon.application.scopes.ActivityScope
import dagger.Subcomponent

/**
 * Created by tyler on 12/14/17.
 */
@ActivityScope
@Subcomponent(modules = [(AddFriendModule::class)])
interface AddFriendComponent {

  fun inject(addFriendActivity: AddFriendActivity)

}
