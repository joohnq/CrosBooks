package com.joohnq.crosbooks.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.crosbooks.common.exceptions.CustomException
import com.joohnq.crosbooks.constants.AppConstants
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.model.entities.BookPost
import com.joohnq.crosbooks.model.network.books.BooksRepositoryImpl
import com.joohnq.crosbooks.view.state.UiState
import com.joohnq.crosbooks.view.state.UiState.Companion.onSuccess
import com.joohnq.crosbooks.view.state.UiState.Companion.value
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BooksViewModel(
    private val booksRepositoryImpl: BooksRepositoryImpl
) : ViewModel() {
    private val _maxPageHome: MutableLiveData<Int> =
        MutableLiveData(1)
    private val maxPageHome: LiveData<Int> get() = _maxPageHome

    private val _currentPage: MutableLiveData<Int> =
        MutableLiveData(1)
    private val currentPage: LiveData<Int> get() = _currentPage

    private val _books: MutableLiveData<UiState<MutableList<Book>>> = MutableLiveData(UiState.Idle)
    val books: LiveData<UiState<MutableList<Book>>> get() = _books

//    fun getBooks() = viewModelScope.launch {
////        if (currentPage.value == maxPageHome.value) return@launch
//        val oldBooks = books.value.value()
//        _books.value = UiState.Loading
//        val page = currentPage.value?.plus(1) ?: 1
//        viewModelScope.launch {
//            booksRepositoryImpl.getBooks(page).catch {
//                _books.value = UiState.Error(it.message.toString())
//            }.collect { res ->
//                oldBooks.addAll(res.data)
//                _books.value = UiState.Success(oldBooks)
//                _maxPageHome.value = res.totalPages
//                _currentPage.value = res.page
//            }
//        }
//    }

    fun getBooksPagination() = viewModelScope.launch {
        val current = currentPage.value ?: 1
        val maxPage = maxPageHome.value ?: 1
        if (current == maxPage || current.plus(1) > maxPage) return@launch

        val oldBooks = books.value.value()
        _books.value = UiState.Loading
        val page = current.plus(1)
        viewModelScope.launch {
            booksRepositoryImpl.getBooks(page).catch {
                _books.value = UiState.Error(it.message.toString())
            }.collect { res ->
                oldBooks.addAll(res.data)
                _books.value = UiState.Success(oldBooks)
                _maxPageHome.value = res.totalPages
                _currentPage.value = res.page
            }
        }
    }

    fun getAllBooks() = viewModelScope.launch {
        resetPageValues()
        _books.value = UiState.Loading
        val currentPage = currentPage.value ?: 1
        booksRepositoryImpl.getBooks(currentPage).catch {
            _books.value = UiState.Error(it.message.toString())
        }.collect { res ->
            _books.value = UiState.Success(res.data.toMutableList())
            val totalPages = res.totalPages
            val page = res.page
            _maxPageHome.value = totalPages
            _currentPage.value = page
        }
    }

    suspend fun sendBookImage(bookImage: Uri): String =
        try {
            val res = booksRepositoryImpl.sendBookImage(bookImage).first()
            res.url
        } catch (e: Exception) {
            throw e
        }

    suspend fun addBook(bookPost: BookPost): Book = try {
        booksRepositoryImpl.addBook(bookPost).first()
    } catch (e: Exception) {
        throw CustomException.BookNotAdded()
    }

    suspend fun removeBook(id: Int): Boolean = try {
        booksRepositoryImpl.removeBook(id).first()
        true
    } catch (e: Exception) {
        throw e
    }

    private fun resetPageValues() = viewModelScope.launch {
        _currentPage.value = 1
        _maxPageHome.value = 1
    }

    suspend fun getBookDetail(id: Int): Book = try {
        booksRepositoryImpl.getBookDetail(id).first()
    } catch (e: Exception) {
        throw e
    }
}