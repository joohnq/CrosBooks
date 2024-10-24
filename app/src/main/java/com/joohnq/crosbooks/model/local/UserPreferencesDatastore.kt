package com.joohnq.crosbooks.model.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.preferencesDataStore by preferencesDataStore(name = "user_preferences")

fun getUserPreferencesDatastore(context: Context): DataStore<Preferences> {
    return context.preferencesDataStore
}

fun DataStore<Preferences>.getToken(): Flow<String?> {
    return data.map { userPreferences -> userPreferences[TOKEN_KEY] }
}
