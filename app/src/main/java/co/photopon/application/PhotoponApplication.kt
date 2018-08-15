package co.photopon.application

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.parse.Parse

class PhotoponApplication: Application() {
  private val appComponent: AppComponent by lazy {
    DaggerAppComponent
        .builder()
        .appModule(AppModule(this))
        .build()
  }

  override fun onCreate() {
    super.onCreate()

    inject()

    initParse()
    init310()
  }

  fun appComponent() : AppComponent = appComponent

  private fun inject() {
    appComponent.inject(this)
  }

  private fun init310() {
    AndroidThreeTen.init(this)
  }

  private fun initParse() {
    Parse.initialize(Parse.Configuration.Builder(this)
        .applicationId("qyY21OT36AiP5hIEdrzrBvbOS1HgXzIK52oyzrAN")
        .clientKey("CwOKephJcNOFokOWx6X2wgDO2eOKDGL2lXfYgPCC")
        .server("https://photopon.herokuapp.com/parse")
        .build()
    )
  }
}
