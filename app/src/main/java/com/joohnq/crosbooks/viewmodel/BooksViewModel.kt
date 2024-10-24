package com.joohnq.crosbooks.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.crosbooks.UiState
import com.joohnq.crosbooks.UiState.Companion.onSuccess
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.model.entities.BookPost
import com.joohnq.crosbooks.model.network.books.BooksRepositoryImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BooksViewModel(
    private val booksRepositoryImpl: BooksRepositoryImpl
) : ViewModel() {
    private val _maxPageHome: MutableLiveData<Int> =
        MutableLiveData(1)
    val maxPageHome: LiveData<Int> get() = _maxPageHome

    private val _currentPageHome: MutableLiveData<Int> =
        MutableLiveData(1)
    val currentPageHome: LiveData<Int> get() = _currentPageHome

    private val _addCurrentBookStatus: MutableLiveData<UiState<Boolean>> =
        MutableLiveData(UiState.Idle)
    val addCurrentBookStatus: LiveData<UiState<Boolean>> get() = _addCurrentBookStatus

    private val _books: MutableLiveData<UiState<MutableList<Book>>> = MutableLiveData(UiState.Idle)
    val books: LiveData<UiState<MutableList<Book>>> get() = _books

    private val _searchingBooks: MutableLiveData<UiState<MutableList<Book>>> = MutableLiveData(UiState.Idle)
    val searchingBooks: LiveData<UiState<MutableList<Book>>> get() = _searchingBooks

    fun getBooks() = viewModelScope.launch {
        _books.value = UiState.Loading
        booksRepositoryImpl.getBooks().catch {
            _books.value = UiState.Error(it.message.toString())
        }.collect { res ->
            _books.value = UiState.Success(res.data.toMutableList())
            _maxPageHome.value = res.totalPages
            _currentPageHome.value = res.page
        }
    }

    suspend fun sendBookImage(bookImage: Uri): String =
        try {
            val res = booksRepositoryImpl.sendBookImage(bookImage).first()
            res.url
        } catch (e: Exception) {
            throw e
        }

    suspend fun addBook(bookPost: BookPost): Boolean = try {
        booksRepositoryImpl.addBook(bookPost).first()
        true
    } catch (e: Exception) {
        throw e
    }

    suspend fun removeBook(id: Int): Boolean = try {
        booksRepositoryImpl.removeBook(id).first()
        true
    } catch (e: Exception) {
        throw e
    }

    fun removeBookFromList(id: Int) = viewModelScope.launch {
        _books.value?.onSuccess { data ->
            _books.value = UiState.Loading
            _books.value = UiState.Success(data.filter { book -> book.id != id }.toMutableList())
        }
    }

    suspend fun getBookDetail(id: Int): Book = try {
        booksRepositoryImpl.getBookDetail(id).first()
    } catch (e: Exception) {
        throw e
    }
}