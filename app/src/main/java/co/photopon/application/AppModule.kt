package co.photopon.application

import android.content.Context
import co.photopon.managers.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module
class AppModule(val app: PhotoponApplication) {

  @Provides
  @Singleton
  fun provideApp(): PhotoponApplication = app

  @Provides
  @Singleton
  fun provideContext(): Context = app.applicationContext

  @Provides
  @Singleton
  fun provideSmsManager(): SmsManager = SmsManager()

  @Provides
  @Singleton
  fun provideUserManager(): UserManager = UserManager()

  @Provides
  @Singleton
  fun providePreferencesManager(): PreferencesManager = PreferencesManager()

  @Provides
  @Singleton
  fun provideFriendManager(): FriendManager = FriendManager()

  @Provides
  @Singleton
  fun provideGiftManager(): GiftManager = GiftManager()

  @Provides
  @Singleton
  fun provideNotificationManager(): NotificationManager = NotificationManager()
}
