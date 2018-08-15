package co.photopon.application

import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import co.photopon.R

abstract class PhotoponFragment : Fragment() {

  fun showCancelableDialog(titleResource: Int, message: String, positiveButton: Int, positiveAction: () -> Unit) {
    AlertDialog.Builder(activity!!, R.style.PhotoponTheme)
        .setTitle(titleResource)
        .setMessage(message)
        .setPositiveButton(positiveButton, { dialog, _ ->
          positiveAction()
          dialog.dismiss()
        })
        .setNegativeButton(R.string.cancel, { dialog, _ -> dialog.dismiss() })
        .setCancelable(true)
        .show()
  }

}
