package com.joohnq.crosbooks.model.network.auth

import com.joohnq.crosbooks.model.entities.TokenResponse
import com.joohnq.crosbooks.model.entities.UserLoginPost
import com.joohnq.crosbooks.model.entities.UserRegisterPost
import com.joohnq.crosbooks.model.entities.UserResponse
import com.joohnq.crosbooks.model.network.NetworkHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {
    override fun register(
        userRegisterPost: UserRegisterPost
    ): Flow<UserResponse> {
        return flow {
            try {
                val res = authService.register(userRegisterPost)
                val user = NetworkHelper.handleNetworkErrors(res)
                emit(user)
            } catch (e: Exception) {
                throw e
            }
        }.flowOn(ioDispatcher)
    }

    override fun login(
        userLoginPost: UserLoginPost
    ): Flow<TokenResponse> {
        return flow {
            try {
                val res = authService.login(userLoginPost)
                val user = NetworkHelper.handleNetworkErrors(res)
                emit(user)
            } catch (e: Exception) {
                throw e
            }
        }.flowOn(ioDispatcher)
    }
}