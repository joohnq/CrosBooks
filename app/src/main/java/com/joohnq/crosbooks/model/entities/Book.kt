package com.joohnq.crosbooks.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val author: String,
    val category: Category,
    val createdAt: String,
    val id: Int,
    val imageUrl: String,
    val summary: String,
    val title: String,
    val updatedAt: String
): Parcelable