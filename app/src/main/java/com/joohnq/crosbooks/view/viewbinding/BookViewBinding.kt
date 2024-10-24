package com.joohnq.crosbooks.view.viewbinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BookViewBinding {
    @BindingAdapter("image")
    @JvmStatic
    fun setBookImage(imageView: ImageView, imageUrl: String?) {
       Glide.with(imageView.context).load(imageUrl).into(imageView)
    }
}