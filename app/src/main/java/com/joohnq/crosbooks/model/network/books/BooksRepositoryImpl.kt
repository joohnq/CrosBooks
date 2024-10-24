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


interface BooksRepository {
    fun getBooks(): Flow<BooksResponse>
    fun sendBookImage(
        uri: Uri
    ): Flow<BookImageResponse>

    fun addBook(bookPost: BookPost): Flow<Book>
    fun removeBook(id: Int): Flow<Boolean>
    fun getBookDetail(id: Int): Flow<Book>
}

class BooksRepositoryImpl(
    private val booksService: BooksService,
    private val ioDispatcher: CoroutineDispatcher,
    private val context: Context
) : BooksRepository {
    override fun getBooks(
    ): Flow<BooksResponse> {
        return flow {
            try {
                val res = booksService.getBooks()
                val resBody = NetworkHelper.extractErrorFromJson(res.errorBody()?.string())
                if (resBody != null) throw Exception(resBody)
                if (!res.isSuccessful) throw Exception(res.message().toString())
                val books = res.body() ?: throw Exception("Books is null")
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
                val resBody = NetworkHelper.extractErrorFromJson(res.errorBody()?.string())
                if (resBody != null) throw Exception(resBody)
                if (!res.isSuccessful) throw Exception(res.message().toString())
                val url = res.body() ?: throw Exception("Url is null")
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
                val resBody = NetworkHelper.extractErrorFromJson(res.errorBody()?.string())
                if (resBody != null) throw Exception(resBody)
                if (!res.isSuccessful) throw Exception(res.message().toString())
                val url = res.body() ?: throw Exception("Body is null")
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
                val resBody = NetworkHelper.extractErrorFromJson(res.errorBody()?.string())
                if (resBody != null) throw Exception(resBody)
                if (!res.isSuccessful) throw Exception(res.message().toString())
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
                val resBody = NetworkHelper.extractErrorFromJson(res.errorBody()?.string())
                if (resBody != null) throw Exception(resBody)
                if (!res.isSuccessful) throw Exception(res.message().toString())
                val book = res.body() ?: throw Exception("Book is null")
                emit(book)
            } catch (e: Exception) {
                throw e
            }
        }.flowOn(ioDispatcher)
    }
}
