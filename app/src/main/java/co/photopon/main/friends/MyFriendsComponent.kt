package co.photopon.main.friends

import co.photopon.application.scopes.ActivityScope
import dagger.Subcomponent

/**
 * Created by tyler on 12/14/17.
 */
@ActivityScope
@Subcomponent(modules = [(MyFriendsModule::class)])
interface MyFriendsComponent {

  fun inject(myFriendsFragment: MyFriendsFragment)

}
