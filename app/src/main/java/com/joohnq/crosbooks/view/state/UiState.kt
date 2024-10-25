package com.joohnq.crosbooks.view.state

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    data object Idle : UiState<Nothing>()

    companion object {
        fun <T> UiState<T>.fold(
            onLoading: () -> Unit = {},
            onIdle: () -> Unit = {},
            onSuccess: (T) -> Unit = {},
            onError: (String) -> Unit = {}
        ) {
            when (this) {
                is Loading -> onLoading()
                is Success -> onSuccess(this.data)
                is Error -> onError(this.message)
                Idle -> onIdle()
            }
        }

        fun <T> UiState<T>.onSuccess(
            onSuccess: (T) -> Unit = {},
        ) {
            when (this) {
                is Success -> onSuccess(this.data)
                else -> {}
            }
        }

        fun <T> UiState<T>.onLoading(
            onLoading: () -> Unit
        ) {
            when (this) {
                is Loading -> onLoading()
                else -> {}
            }
        }

        fun <T> UiState<T>.onError(
            onError: (String) -> Unit
        ) {
            when (this) {
                is Error -> onError(this.message)
                else -> {}
            }
        }

        fun <T> UiState<List<T>>.toRecyclerViewState(
            take: Int? = null,
            onError: (String) -> Unit
        ): RecyclerViewState<T> {
            return when (this) {
                is Success<List<T>> -> {
                    return if (data.isEmpty())
                        RecyclerViewState.Empty
                    else RecyclerViewState.Success(data.take(take ?: data.size))
                }

                is Error -> {
                    onError(message)
                    RecyclerViewState.Error(message)
                }

                is Loading -> {
                    RecyclerViewState.Loading
                }

                is Idle -> RecyclerViewState.Idle
            }
        }

        fun <T> UiState<MutableList<T>>?.value(): MutableList<T> = when (this) {
            is Success<MutableList<T>> -> {
                this.data
            }

            else -> mutableListOf()
        }
    }
}

