package com.joohnq.crosbooks.model.entities

import kotlinx.serialization.Serializable

data class UserRegisterPost(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)