package com.joohnq.crosbooks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.crosbooks.view.state.UiState
import com.joohnq.crosbooks.model.entities.Category
import com.joohnq.crosbooks.model.network.categories.CategoriesRepositoryImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val categoriesRepositoryImpl: CategoriesRepositoryImpl
) : ViewModel() {
    private val _categories: MutableLiveData<UiState<List<Category>>> =
        MutableLiveData(UiState.Idle)
    val categories: LiveData<UiState<List<Category>>> get() = _categories

    fun getCategories() = viewModelScope.launch {
        _categories.value = UiState.Loading
        categoriesRepositoryImpl.getCategories()
            .catch {
                _categories.value = UiState.Error(it.message.toString())
            }.collect { res ->
                _categories.value = UiState.Success(res)
            }
    }
}