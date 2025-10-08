package com.konkuk.medicarecall.data.api

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore(name = PREFS_NAME)

    companion object {
        private const val PREFS_NAME = "app_preferences"
        private val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val CRITICAL_POPUP_KEY = booleanPreferencesKey("critical_popup_enabled")
    }

    val fcmTokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[FCM_TOKEN_KEY]
    }

    val accessTokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[ACCESS_TOKEN_KEY]
    }

    val criticalPopupFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[CRITICAL_POPUP_KEY] ?: false
    }

    suspend fun saveFcmToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[FCM_TOKEN_KEY] = token
        }
    }

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = token
        }
    }

    suspend fun clearAccessToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
        }
    }

    suspend fun setCriticalPopupEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[CRITICAL_POPUP_KEY] = enabled
        }
    }
}
