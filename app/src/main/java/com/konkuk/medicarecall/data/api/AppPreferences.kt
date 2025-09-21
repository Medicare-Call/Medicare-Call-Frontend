package com.konkuk.medicarecall.data.api

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "app_preferences"
        private const val FCM_TOKEN_KEY = "fcm_token"
        private const val ACCESS_TOKEN_KEY = "access_token"

        //  팝업(잠금화면 팝업/풀스크린) 허용 여부
        private const val KEY_CRITICAL_POPUP = "critical_popup_enabled"
    }

    fun saveFcmToken(token: String) = sharedPreferences.edit { putString(FCM_TOKEN_KEY, token) }
    fun getFcmToken(): String? = sharedPreferences.getString(FCM_TOKEN_KEY, null)

    fun saveAccessToken(token: String) = sharedPreferences.edit { putString(ACCESS_TOKEN_KEY, token) }
    fun getAccessToken(): String? = sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    fun clearAccessToken() = sharedPreferences.edit { remove(ACCESS_TOKEN_KEY) }

    // getter/setter
    fun criticalPopupEnabled(): Boolean =
        sharedPreferences.getBoolean(KEY_CRITICAL_POPUP, false)

    fun setCriticalPopupEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_CRITICAL_POPUP, enabled) }
    }
}
