package com.joohnq.crosbooks.model.entities

data class BooksResponse(
    val data: List<Book>,
    val itemsPerPage: Int,
    val page: Int,
    val totalItems: Int,
    val totalPages: Int
)