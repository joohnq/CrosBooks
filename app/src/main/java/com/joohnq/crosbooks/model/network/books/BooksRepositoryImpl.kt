package com.joohnq.crosbooks.model.network.books

import android.content.Context
import android.net.Uri
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.model.entities.BookImageResponse
import com.joohnq.crosbooks.model.entities.BookPost
import com.joohnq.crosbooks.model.entities.BooksResponse
import com.joohnq.crosbooks.model.network.NetworkHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody

class BooksRepositoryImpl(
    private val booksService: BooksService,
    private val ioDispatcher: CoroutineDispatcher,
    private val context: Context
) : BooksRepository {
    override fun getBooks(
        page: Int
    ): Flow<BooksResponse> {
        return flow {
            try {
                val res = booksService.getBooks(page)
                val books = NetworkHelper.handleNetworkErrors(res)
                emit(books)
            } catch (e: Exception) {
                throw e
            }
        }.flowOn(ioDispatcher)
    }

    override fun getBooks(
        search: String,
        page: Int
    ): Flow<BooksResponse> {
        return flow {
            try {
                val res = booksService.getBooks(search, page)
                val books = NetworkHelper.handleNetworkErrors(res)
                emit(books)
            } catch (e: Exception) {
                throw e
            }
        }.flowOn(ioDispatcher)
    }

    override fun sendBookImage(
        uri: Uri
    ): Flow<BookImageResponse> {
        return flow {
            try {
                val body: MultipartBody.Part =
                    NetworkHelper.convertUriToMultipartBodyPart(context, uri)
                val res = booksService.sendBookImage(body)
                val url = NetworkHelper.handleNetworkErrors(res)
                emit(url)
            } catch (e: Exception) {
                throw e
            }
        }.flowOn(ioDispatcher)
    }

    override fun addBook(bookPost: BookPost): Flow<Book> {
        return flow {
            try {
                val res = booksService.addBook(bookPost)
                val url = NetworkHelper.handleNetworkErrors(res)
                emit(url)
            } catch (e: Exception) {
                throw e
            }
        }.flowOn(ioDispatcher)
    }

    override fun removeBook(id: Int): Flow<Boolean> {
        return flow {
            try {
                val res = booksService.removeBook(id)
                NetworkHelper.handleNetworkErrorsWithoutBody(res)
                emit(true)
            } catch (e: Exception) {
                throw e
            }
        }.flowOn(ioDispatcher)
    }

    override fun getBookDetail(id: Int): Flow<Book> {
        return flow {
            try {
                val res = booksService.getBookDetail(id)
                val book = NetworkHelper.handleNetworkErrors(res)
                emit(book)
            } catch (e: Exception) {
                throw e
            }
        }.flowOn(ioDispatcher)
    }
}
