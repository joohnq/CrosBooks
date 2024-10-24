package com.joohnq.crosbooks.common.exceptions

sealed class EmailException(message: String) : Exception(message){
    class EmailInvalid(message: String = "Password invalid") : EmailException(message)
    class EmailEmpty(message: String = "Email cannot be empty") : EmailException(message)
}