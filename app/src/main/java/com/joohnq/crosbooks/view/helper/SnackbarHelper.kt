package com.joohnq.crosbooks.view.helper

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBarHelper {
    operator fun invoke(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
}