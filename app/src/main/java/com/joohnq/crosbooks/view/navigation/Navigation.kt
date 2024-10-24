package com.joohnq.crosbooks.view.navigation

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.view.activities.AddBookActivity
import com.joohnq.crosbooks.view.activities.BookDetailActivity
import com.joohnq.crosbooks.view.activities.HomeActivity

fun AppCompatActivity.navigateToBookDetailActivity(
    book: Book
) {
    Intent(this, BookDetailActivity::class.java).also {
        it.putExtra("book", book)
        startActivity(it)
    }
}

fun AppCompatActivity.navigateToHomeActivity(
    likeNewTask: Boolean = true
) {
    Intent(this, HomeActivity::class.java).also {
        if (likeNewTask) {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(it)
    }
}

fun Fragment.navigateToHomeActivity(
    likeNewTask: Boolean = true
) {
    Intent(this.context, HomeActivity::class.java).also {
        if (likeNewTask) {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(it)
    }
}

fun AppCompatActivity.navigateToActivity(
    activity: Class<*>,
    likeNewTask: Boolean = true
) {
    Intent(this, activity).also {
        if (likeNewTask) {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(it)
    }
}

fun AppCompatActivity.navigateToAddBookActivity() {
    Intent(this, AddBookActivity::class.java).also {
        startActivity(it)
    }
}