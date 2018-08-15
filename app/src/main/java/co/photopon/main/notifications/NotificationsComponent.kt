package co.photopon.main.friends

import co.photopon.application.scopes.ActivityScope
import co.photopon.main.notifications.NotificationsFragment
import dagger.Subcomponent

/**
 * Created by tyler on 12/14/17.
 */
@ActivityScope
@Subcomponent(modules = [(NotificationsModule::class)])
interface NotificationsComponent {

  fun inject(notificationsFragment: NotificationsFragment)

}
