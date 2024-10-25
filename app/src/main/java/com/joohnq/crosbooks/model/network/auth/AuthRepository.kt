package com.joohnq.crosbooks.model.network.auth

import com.joohnq.crosbooks.model.entities.TokenResponse
import com.joohnq.crosbooks.model.entities.UserLoginPost
import com.joohnq.crosbooks.model.entities.UserRegisterPost
import com.joohnq.crosbooks.model.entities.UserResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(userRegisterPost: UserRegisterPost): Flow<UserResponse>
    fun login(userLoginPost: UserLoginPost): Flow<TokenResponse>
}
