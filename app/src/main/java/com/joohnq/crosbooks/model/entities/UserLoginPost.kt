package com.joohnq.crosbooks.model.entities

data class UserLoginPost(
    val credential: String,
    val password: String,
)