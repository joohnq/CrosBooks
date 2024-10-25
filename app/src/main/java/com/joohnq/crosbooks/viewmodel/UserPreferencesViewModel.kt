package com.joohnq.crosbooks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.crosbooks.view.state.UiState
import com.joohnq.crosbooks.model.local.UserPreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class UserPreferencesViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
) :
    ViewModel() {
    private val _token: MutableLiveData<UiState<String?>> = MutableLiveData(UiState.Idle)
    val token: LiveData<UiState<String?>> get() = _token

    fun getToken() = viewModelScope.launch {
        userPreferencesRepository.getToken().catch {
            _token.value = UiState.Error(it.message.toString())
        }.collect {
            _token.value = UiState.Success(it)
        }
    }

    suspend fun setToken(token: String): Boolean = userPreferencesRepository.setToken(token)
}