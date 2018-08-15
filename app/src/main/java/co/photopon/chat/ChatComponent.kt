package co.photopon.chat

import co.photopon.application.scopes.ActivityScope
import dagger.Subcomponent

/**
 * Created by tyler on 12/14/17.
 */
@ActivityScope
@Subcomponent(modules = [(ChatModule::class)])
interface ChatComponent {

  fun inject(chatActivity: ChatActivity)

}
