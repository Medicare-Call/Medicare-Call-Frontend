package com.konkuk.medicarecall.data.repositoryimpl

import androidx.datastore.core.DataStore
import com.konkuk.medicarecall.data.di.TokenDataStore
import com.konkuk.medicarecall.data.model.Token
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Single

@Single
class DataStoreRepositoryImpl(
    @TokenDataStore private val tokenDataStore: DataStore<Token>,
) : DataStoreRepository {

    override suspend fun saveAccessToken(token: String) {
        tokenDataStore.updateData { it.copy(accessToken = token) }
    }

    override suspend fun getAccessToken(): String? {
        val preferences = tokenDataStore.data.first()
        return preferences.accessToken
    }

    override suspend fun saveRefreshToken(token: String) {
        tokenDataStore.updateData {
            it.copy(refreshToken = token)
        }
    }

    override suspend fun getRefreshToken(): String? {
        val preferences = tokenDataStore.data.first()
        return preferences.refreshToken
    }

    override suspend fun clearTokens() {
        tokenDataStore.updateData {
            Token(null, null)
        }
    }
}
