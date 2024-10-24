package com.joohnq.crosbooks.common.exceptions

sealed class PasswordConfirmException(message: String) : Exception(message){
    class PasswordConfirmEmpty(message: String = "Password confirm cannot be empty") : PasswordConfirmException(message)
    class PasswordConfirmNotMatch(message: String = "Password confirm does not match") : PasswordConfirmException(message)
}