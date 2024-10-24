package com.joohnq.crosbooks.view.helper

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.joohnq.crosbooks.databinding.ActivityBookDetailBinding

fun View.showSnackBar(message:String){
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}