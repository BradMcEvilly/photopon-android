package co.photopon.main.make

import co.photopon.application.scopes.ActivityScope
import dagger.Subcomponent

/**
 * Created by tyler on 12/14/17.
 */
@ActivityScope
@Subcomponent(modules = [(MakePhotoponModule::class)])
interface MakePhotoponComponent {

  fun inject(makePhotoponFragment: MakePhotoponFragment)

}
