package co.photopon.main.friends

import android.content.Context
import co.photopon.managers.GiftManager
import dagger.Module
import dagger.Provides

/**
 * Created by tyler on 12/14/17.
 */

@Module
class GiftsModule(val view: GiftsView) {

  @Provides
  fun provideView(): GiftsView = view

  @Provides
  fun providePresenter(view: GiftsView,
                       context: Context,
                       giftsManager: GiftManager): GiftsPresenter =
      GiftsPresenterImpl(view, context, giftsManager)

}
