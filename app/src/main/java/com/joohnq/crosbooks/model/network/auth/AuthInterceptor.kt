package com.joohnq.crosbooks.model.network.auth

import com.joohnq.crosbooks.model.local.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val userPreferencesRepository: UserPreferencesRepository) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requiresAuth = originalRequest.header("Requires-Auth") != null
        val token = if (requiresAuth) {
            runBlocking {
                userPreferencesRepository.getToken().first()
            }
        } else {
            null
        }

        val requestBuilder = originalRequest.newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}