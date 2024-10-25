package com.joohnq.crosbooks.view.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.view.activities.AddBookActivity
import com.joohnq.crosbooks.view.activities.AuthActivity
import com.joohnq.crosbooks.view.activities.BookDetailActivity
import com.joohnq.crosbooks.view.activities.HomeActivity

fun Fragment.navigateToBookDetailActivity(
    book: Book
) {
    Intent(this.context, BookDetailActivity::class.java).also {
        it.putExtra("book", book)
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

fun Fragment.navigateToAddBookActivity() {
    Intent(this.context, AddBookActivity::class.java).also {
        startActivity(it)
    }
}

fun Fragment.navigateToAuthActivity() {
    Intent(this.context, AuthActivity::class.java).also {
        startActivity(it)
    }
}