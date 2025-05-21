package com.example.setoranhapalandosen.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("user_preferences")

class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    // Key untuk token, refresh token, dan data profil

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NIM_KEY = stringPreferencesKey("nim")
    }

    // =
    // === SAVE FUNCTIONS ===
    // =

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        context.dataStore.edit { it[REFRESH_TOKEN_KEY] = refreshToken }
    }

    suspend fun saveUserProfile(name: String, email: String, nim: String) {
        context.dataStore.edit {
            it[NAME_KEY] = name
            it[EMAIL_KEY] = email
            it[NIM_KEY] = nim
        }
    }

    // =
    // === READ FUNCTIONS ===
    // =

    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
}