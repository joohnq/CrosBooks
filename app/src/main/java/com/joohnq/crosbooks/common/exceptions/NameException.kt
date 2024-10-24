package com.joohnq.crosbooks.common.exceptions

sealed class NameException(message: String) : Exception(message){
    class NameEmpty(message: String = "Name cannot be empty") : NameException(message)
}