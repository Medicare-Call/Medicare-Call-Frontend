package com.konkuk.medicarecall.data.repositoryimpl

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.konkuk.medicarecall.data.model.Token
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.util.TokenSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.tokenDataStore by dataStore(
    fileName = "tokens",
    serializer = TokenSerializer,
    corruptionHandler = ReplaceFileCorruptionHandler {
        // 기존 암호문이 깨졌을 때 기본값으로 복원
        Token(null, null)
    },
)

@Singleton
class DataStoreRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) :
    DataStoreRepository {

    override suspend fun saveAccessToken(token: String) {
        context.tokenDataStore.updateData { it.copy(accessToken = token) }
    }

    override suspend fun getAccessToken(): String? {
        val preferences = context.tokenDataStore.data.first()
        return preferences.accessToken
    }

    override suspend fun saveRefreshToken(token: String) {
        context.tokenDataStore.updateData {
            it.copy(refreshToken = token)
        }
    }

    override suspend fun getRefreshToken(): String? {
        val preferences = context.tokenDataStore.data.first()
        return preferences.refreshToken
    }

    override suspend fun clearTokens() {
        context.tokenDataStore.updateData {
            Token(null, null)
        }
    }
}
