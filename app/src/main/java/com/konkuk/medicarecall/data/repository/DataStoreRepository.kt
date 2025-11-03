package com.konkuk.medicarecall.data.repository


interface DataStoreRepository {
    suspend fun saveAccessToken(token: String)
    suspend fun getAccessToken(): String?
    suspend fun saveRefreshToken(token: String)
    suspend fun getRefreshToken(): String?
    suspend fun clearTokens()

    // fcm 관련
    suspend fun saveFcmAccessToken(token: String)
    suspend fun getFcmAccessToken(): String?
    suspend fun saveFcmToken(token: String)
    suspend fun getFcmToken(): String?
}
