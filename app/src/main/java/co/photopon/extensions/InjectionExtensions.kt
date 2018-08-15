package co.photopon.extensions

import android.app.Activity
import android.support.v4.app.Fragment
import co.photopon.application.PhotoponApplication

/**
 * Created by tyler on 12/14/17.
 */

val Activity.mainApplication: PhotoponApplication
  get() = application as PhotoponApplication

val Fragment.mainApplication: PhotoponApplication
  get() = activity?.application as PhotoponApplication
