package com.joohnq.crosbooks.model.network.books

import android.util.Log
import com.joohnq.crosbooks.constants.AppConstants
import com.joohnq.crosbooks.model.entities.BooksResponse
import com.joohnq.crosbooks.model.network.NetworkHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface BooksRepository {
    fun getBooks(): Flow<BooksResponse>
}

class BooksRepositoryImpl(
    private val booksService: BooksService,
    private val ioDispatcher: CoroutineDispatcher
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
}
