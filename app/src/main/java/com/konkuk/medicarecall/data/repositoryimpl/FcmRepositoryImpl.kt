package com.konkuk.medicarecall.data.repositoryimpl

import com.konkuk.medicarecall.data.repository.DataStoreRepository
import com.konkuk.medicarecall.data.repository.FcmRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmRepositoryImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): FcmRepository {
    override suspend fun saveFcmAccessToken(token: String) {
        dataStoreRepository.saveFcmAccessToken(token)
    }

    override suspend fun getFcmAccessToken(): String? {
        return dataStoreRepository.getFcmAccessToken()
    }

    override suspend fun saveFcmToken(token: String) {
        dataStoreRepository.saveFcmToken(token)
    }

    override suspend fun getFcmToken(): String? {
        return dataStoreRepository.getFcmToken()
    }
}
