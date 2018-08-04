package co.photopon.application

import co.photopon.friends.AddFriendComponent
import co.photopon.friends.AddFriendModule
import co.photopon.friends.ShareGiftComponent
import co.photopon.friends.ShareGiftModule
import co.photopon.main.draw.DrawCouponComponent
import co.photopon.main.draw.DrawCouponModule
import co.photopon.main.friends.*
import co.photopon.main.make.MakePhotoponComponent
import co.photopon.main.make.MakePhotoponModule
import co.photopon.onboarding.verify.VerifyNumberComponent
import co.photopon.onboarding.verify.VerifyNumberModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by tyler on 12/12/17.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

  fun inject(app: PhotoponApplication)

  fun plus(validateModule: VerifyNumberModule): VerifyNumberComponent
  fun plus(addFriendModule: AddFriendModule): AddFriendComponent
  fun plus(shareGiftModule: ShareGiftModule): ShareGiftComponent
  fun plus(myFriendsModule: MyFriendsModule): MyFriendsComponent
  fun plus(giftsModule: GiftsModule): GiftsComponent
  fun plus(notificationsModule: NotificationsModule): NotificationsComponent
  fun plus(makePhotoponModule: MakePhotoponModule): MakePhotoponComponent
  fun plus(drawCouponModule: DrawCouponModule): DrawCouponComponent

}
