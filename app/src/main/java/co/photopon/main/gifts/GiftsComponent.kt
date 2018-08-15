package co.photopon.main.friends

import co.photopon.application.scopes.ActivityScope
import co.photopon.main.gifts.GiftsFragment
import dagger.Subcomponent

/**
 * Created by tyler on 12/14/17.
 */
@ActivityScope
@Subcomponent(modules = [(GiftsModule::class)])
interface GiftsComponent {

  fun inject(giftsFragment: GiftsFragment)

}
