package com.joohnq.crosbooks.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val createdAt: String,
    val id: Int,
    val title: String,
    val updatedAt: String
) : Parcelable