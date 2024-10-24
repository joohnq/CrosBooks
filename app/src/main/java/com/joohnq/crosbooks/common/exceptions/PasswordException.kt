package com.joohnq.crosbooks.common.exceptions

sealed class PasswordException(message: String) : Exception(message){
    class PasswordTooShort(message: String = "Password is too short") : PasswordException(message)
    class PasswordEmpty(message: String = "Password cannot be empty") : PasswordException(message)
}