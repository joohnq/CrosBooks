package com.joohnq.crosbooks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.model.network.books.BooksRepositoryImpl
import com.joohnq.crosbooks.view.state.UiState
import com.joohnq.crosbooks.view.state.UiState.Companion.onSuccess
import com.joohnq.crosbooks.view.state.UiState.Companion.value
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchViewModel(
    private val booksRepositoryImpl: BooksRepositoryImpl
) : ViewModel() {
    private val _maxPageHome: MutableLiveData<Int> =
        MutableLiveData(1)
    private val maxPageHome: LiveData<Int> get() = _maxPageHome

    private val _currentPage: MutableLiveData<Int> =
        MutableLiveData(1)
    private val currentPage: LiveData<Int> get() = _currentPage

    private val _searchBooks: MutableLiveData<UiState<MutableList<Book>>> =
        MutableLiveData(UiState.Idle)
    val searchBooks: LiveData<UiState<MutableList<Book>>> get() = _searchBooks

//    fun getSearchBooks(search: String) = viewModelScope.launch {
//        if (currentPage.value == maxPageHome.value) return@launch
//        val oldBooks = searchBooks.value.value()
//        _searchBooks.value = UiState.Loading
//        val page = currentPage.value?.plus(1) ?: 1
//        executeGetSearchBooksFlow(search, page, oldBooks)
//    }

    fun getBooksPagination(search: String) = viewModelScope.launch {
        val current = currentPage.value ?: 1
        val maxPage = maxPageHome.value ?: 1
        if (current == maxPage || current.plus(1) > maxPage) return@launch

        val oldBooks = searchBooks.value.value()
        _searchBooks.value = UiState.Loading
        val page = current.plus(1)
        viewModelScope.launch {
            booksRepositoryImpl.getBooks(search, page).catch {
                _searchBooks.value = UiState.Error(it.message.toString())
            }.collect { res ->
                oldBooks.addAll(res.data)
                _searchBooks.value = UiState.Success(oldBooks)
                _maxPageHome.value = res.totalPages
                _currentPage.value = res.page
            }
        }
    }

    fun getAllSearchBooks(search: String) = viewModelScope.launch {
        resetPageValues()
        _searchBooks.value = UiState.Loading
        val currentPage = currentPage.value ?: 1
        booksRepositoryImpl.getBooks(search, currentPage).catch {
            _searchBooks.value = UiState.Error(it.message.toString())
        }.collect { res ->
            _searchBooks.value = UiState.Success(res.data.toMutableList())
            val totalPages = res.totalPages
            val page = res.page
            _maxPageHome.value = totalPages
            _currentPage.value = page
        }
    }

    private fun executeGetSearchBooksFlow(
        search: String,
        page: Int,
        initialList: MutableList<Book>
    ) = viewModelScope.launch {
        booksRepositoryImpl.getBooks(search, page).catch {
            _searchBooks.value = UiState.Error(it.message.toString())
        }.collect { res ->
            initialList.addAll(res.data)
            _searchBooks.value = UiState.Success(initialList)
            _maxPageHome.value = res.totalPages
            _currentPage.value = res.page
        }
    }

    fun resetPageValues() {
        _currentPage.value = 0
        _maxPageHome.value = 1
    }

    fun removeBookFromList(id: Int) = viewModelScope.launch {
        _searchBooks.value?.onSuccess { data ->
            _searchBooks.value = UiState.Loading
            _searchBooks.value =
                UiState.Success(data.filter { book -> book.id != id }.toMutableList())
        }
    }
}