package com.konkuk.medicarecall.data.repository

interface FcmRepository {
    suspend fun saveFcmAccessToken(token: String)
    suspend fun getFcmAccessToken(): String?
    suspend fun saveFcmToken(token: String)
    suspend fun getFcmToken(): String?
}
