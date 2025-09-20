package com.konkuk.medicarecall.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import com.konkuk.medicarecall.data.model.Token
import com.konkuk.medicarecall.data.util.TokenSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.tokenDataStore by dataStore(
    fileName = "tokens",
    serializer = TokenSerializer,
)

// Token 값을 불러오려면 HiltViewModel 에서
// DataStoreRepository를 받아
// getToken() 함수를 호출하면 됩니다.
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun saveAccessToken(token: String) {
        context.tokenDataStore.updateData { it.copy(accessToken = token) }

    }

    suspend fun getAccessToken(): String? {
        val preferences = context.tokenDataStore.data.first()
        return preferences.accessToken
    }

    suspend fun saveRefreshToken(token: String) {
        context.tokenDataStore.updateData {
            it.copy(refreshToken = token)
        }
    }

    suspend fun getRefreshToken(): String? {
        val preferences = context.tokenDataStore.data.first()
        return preferences.refreshToken
    }

    suspend fun clearTokens() {
        context.tokenDataStore.updateData {
            Token(null, null)
        }
    }
}
