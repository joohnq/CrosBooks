package com.joohnq.crosbooks.model.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

val TOKEN_KEY = stringPreferencesKey("token")

interface UserPreferencesRepository {
    fun getToken(): Flow<String?>
    suspend fun setToken(token: String): Boolean
}

class UserPreferencesRepositoryImpl(
    private val userPreferencesDatastore: DataStore<Preferences>,
    private val ioDispatcher: CoroutineDispatcher
) : UserPreferencesRepository {
    override fun getToken(): Flow<String?> =
        userPreferencesDatastore.data.map { userPreferences -> userPreferences[TOKEN_KEY] }
            .flowOn(ioDispatcher)

    override suspend fun setToken(token: String): Boolean {
        try {
            userPreferencesDatastore.edit { userPreferences -> userPreferences[TOKEN_KEY] = token }
            return true
        } catch (e: Exception) {
            return false
        }
    }

}