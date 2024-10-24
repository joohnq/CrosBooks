package com.joohnq.crosbooks.model.entities

data class BookPost(
    val author: String,
    val categoryId: Int,
    val imageUrl: String,
    val summary: String,
    val title: String,
)