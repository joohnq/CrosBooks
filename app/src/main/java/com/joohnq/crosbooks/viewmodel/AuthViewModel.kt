package com.joohnq.crosbooks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joohnq.crosbooks.UiState
import com.joohnq.crosbooks.model.entities.UserLoginPost
import com.joohnq.crosbooks.model.entities.UserRegisterPost
import com.joohnq.crosbooks.model.network.auth.AuthRepositoryImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AuthViewModel(private val authRepositoryImpl: AuthRepositoryImpl) : ViewModel() {
    private val _register: MutableLiveData<UiState<Boolean>> = MutableLiveData(UiState.Idle)
    val register: LiveData<UiState<Boolean>> get() = _register

    private val _login: MutableLiveData<UiState<Boolean>> = MutableLiveData(UiState.Idle)
    val login: LiveData<UiState<Boolean>> get() = _login

    fun register(name: String, email: String, password: String, confirmPassword: String) =
        viewModelScope.launch {
            _register.value = UiState.Loading
            authRepositoryImpl.register(
                UserRegisterPost(
                    name,
                    email,
                    password,
                    confirmPassword
                )
            ).catch {
                _register.value = UiState.Error(it.message.toString())
            }.collect {
                _register.value = UiState.Success(true)
            }
        }

    suspend fun login(email: String, password: String): String = suspendCancellableCoroutine {continuation ->
        viewModelScope.launch {
            _login.value = UiState.Loading
            authRepositoryImpl.login(
                UserLoginPost(
                    email,
                    password,
                )
            ).catch {
                _login.value = UiState.Error(it.message.toString())
            }.collect {
                _login.value = UiState.Success(true)
                continuation.resume(it.token)
            }
        }
    }
}