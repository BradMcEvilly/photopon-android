package co.photopon.main.friends

import android.content.Context
import co.photopon.managers.NotificationManager
import dagger.Module
import dagger.Provides

/**
 * Created by tyler on 12/14/17.
 */

@Module
class NotificationsModule(val view: NotificationsView) {

  @Provides
  fun provideView(): NotificationsView = view

  @Provides
  fun providePresenter(view: NotificationsView,
                       context: Context,
                       notifificationManager: NotificationManager): NotificationsPresenter =
      NotificationsPresenterImpl(view, context, notifificationManager)

}
