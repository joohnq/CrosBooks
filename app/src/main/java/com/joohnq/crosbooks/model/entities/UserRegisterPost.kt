package com.joohnq.crosbooks.model.entities

data class UserRegisterPost(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)