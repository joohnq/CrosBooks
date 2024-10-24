package com.joohnq.crosbooks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.crosbooks.UiState
import com.joohnq.crosbooks.model.entities.Book
import com.joohnq.crosbooks.model.network.books.BooksRepositoryImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BooksViewModel(
    private val booksRepositoryImpl: BooksRepositoryImpl
) : ViewModel() {
//    private val _booksResponse: MutableLiveData<MutableList<UiState<Book>>> =
//        MutableLiveData(mutableListOf())
//    val booksResponse: LiveData<MutableList<UiState<Book>>>> get() = _booksResponse

    private val _addCurrentBookStatus: MutableLiveData<UiState<Boolean>> = MutableLiveData(UiState.Idle)
    val addCurrentBookStatus: LiveData<UiState<Boolean>> get() = _addCurrentBookStatus

    private val _books: MutableLiveData<UiState<List<Book>>> = MutableLiveData(UiState.Idle)
    val books: LiveData<UiState<List<Book>>> get() = _books

    fun getBooks() = viewModelScope.launch {
        _books.value = UiState.Loading
        booksRepositoryImpl.getBooks().catch {
            _books.value = UiState.Error(it.message.toString())
        }.collect { res ->
            _books.value = UiState.Success(res.data)
        }
    }
}