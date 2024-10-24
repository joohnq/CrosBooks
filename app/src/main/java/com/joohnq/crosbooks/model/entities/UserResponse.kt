package com.joohnq.crosbooks.model.entities

data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val createdAt: String,
    val updatedAt: String
)