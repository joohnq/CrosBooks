package com.joohnq.crosbooks.model.network.books

import android.net.Uri
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.model.entities.BookImageResponse
import com.joohnq.crosbooks.model.entities.BookPost
import com.joohnq.crosbooks.model.entities.BooksResponse
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    fun getBooks(page: Int): Flow<BooksResponse>
    fun getBooks(
        search: String,
        page: Int
    ): Flow<BooksResponse>

    fun sendBookImage(
        uri: Uri
    ): Flow<BookImageResponse>

    fun addBook(bookPost: BookPost): Flow<Book>
    fun removeBook(id: Int): Flow<Boolean>
    fun getBookDetail(id: Int): Flow<Book>
}
