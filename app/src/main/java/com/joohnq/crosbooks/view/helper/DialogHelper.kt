package com.joohnq.crosbooks.view.helper

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object DialogHelper {
    fun areYourSure(
        context: Context,
        title: String,
        onOK: (DialogInterface) -> Unit
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setPositiveButton("OK") { dialog, _ ->
            onOK(dialog)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}