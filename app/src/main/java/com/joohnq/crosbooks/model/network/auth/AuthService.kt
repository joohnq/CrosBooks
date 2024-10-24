package com.joohnq.crosbooks.model.network.auth

import com.joohnq.crosbooks.model.entities.TokenResponse
import com.joohnq.crosbooks.model.entities.UserLoginPost
import com.joohnq.crosbooks.model.entities.UserRegisterPost
import com.joohnq.crosbooks.model.entities.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/users")
    suspend fun register(
        @Body userRegisterPost: UserRegisterPost
    ): Response<UserResponse>

    @POST("/auth/login")
    suspend fun login(
        @Body userLoginPost: UserLoginPost
    ): Response<TokenResponse>
}