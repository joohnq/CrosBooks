package com.joohnq.crosbooks.common.exceptions

sealed class SimpleException(message: String) : Exception(message) {
    class NameCannotBeEmpty(message: String = "Name cannot be empty") :
        SimpleException(message)
    class TitleCannotBeEmpty(message: String = "Title cannot be empty") :
        SimpleException(message)
    class AuthorCannotBeEmpty(message: String = "Author cannot be empty") :
        SimpleException(message)
    class SummaryCannotBeEmpty(message: String = "Summary cannot be empty") :
        SimpleException(message)
    class BookImageNotAdded(message: String = "Please add the book image") :
        SimpleException(message)
}