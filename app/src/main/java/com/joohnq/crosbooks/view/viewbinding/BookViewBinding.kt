package com.joohnq.crosbooks.view.viewbinding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.joohnq.crosbooks.R
import com.joohnq.crosbooks.view.helper.DateTimeHelper

object BookViewBinding {
    @BindingAdapter("image")
    @JvmStatic
    fun setBookImage(imageView: ImageView, imageUrl: String) {
        Glide.with(imageView.context).load(imageUrl).into(imageView)
    }

    @BindingAdapter("author")
    @JvmStatic
    fun setAuthor(textView: TextView, author: String) {
        textView.text = textView.context.getString(
            R.string.author_show,
            author
        )
    }

    @BindingAdapter("createdAt")
    @JvmStatic
    fun setCreatedAt(textView: TextView, createdAt: String) {
        val formattedDate = DateTimeHelper.formatDate(createdAt)
        textView.text = formattedDate
    }

    @BindingAdapter("category")
    @JvmStatic
    fun setCategory(textView: TextView, category: String) {
        textView.text = category
    }
}