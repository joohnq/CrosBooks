package com.joohnq.crosbooks.common.exceptions

sealed class CustomException(message: String) : Exception(message) {
    class NameCannotBeEmpty(message: String = "Name cannot be empty") :
        CustomException(message)
    class TitleCannotBeEmpty(message: String = "Title cannot be empty") :
        CustomException(message)
    class AuthorCannotBeEmpty(message: String = "Author cannot be empty") :
        CustomException(message)
    class SummaryCannotBeEmpty(message: String = "Summary cannot be empty") :
        CustomException(message)
    class BookImageNotAdded(message: String = "Please add the book image") :
        CustomException(message)
    class CategoryNotSelected(message: String = "Category not selected") :
        CustomException(message)
    class BookNotAdded(message: String = "Something went wrong when adding the book") :
        CustomException(message)
    class LogoutFailed(message: String = "Logout failed") :
        CustomException(message)
}