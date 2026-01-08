package com.konkuk.medicarecall.data.repositoryimpl

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import com.google.firebase.messaging.FirebaseMessaging
import com.konkuk.medicarecall.data.api.fcm.FcmUpdateService
import com.konkuk.medicarecall.data.api.fcm.FcmValidationService
import com.konkuk.medicarecall.data.model.FcmToken
import com.konkuk.medicarecall.data.repository.FcmRepository
import com.konkuk.medicarecall.data.util.FcmTokenSerializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single

val Context.fcmDataStore by dataStore(
    fileName = "fcm_tokens",
    serializer = FcmTokenSerializer,
)

@Single
class FcmRepositoryImpl(
    private val context: Context,
    private val fcmValidationService: FcmValidationService,
    private val fcmUpdateService: FcmUpdateService,
) : FcmRepository {
    override suspend fun saveFcmToken(token: String) { // fcm 토큰 저장
        context.fcmDataStore.updateData { it.copy(fcmToken = token) }
    }
    override suspend fun getFcmToken(): String? { // fcm 토큰 불러오기
        val preferences = context.fcmDataStore.data.first()
        return preferences.fcmToken
    }
    override suspend fun clearToken() {
        context.fcmDataStore.updateData {
            FcmToken(null)
        }
    }
    override suspend fun validateAndRefreshTokenIfNeeded(jwtToken: String) {
        try {
            val currentToken = getFcmToken()
            Log.d("FcmRepositoryImpl", "현재 저장된 FCM 토큰: $currentToken")

            // 서버에 토큰 유효성 검사 요청
            val isValid = runCatching {
                fcmValidationService.validateToken().isSuccessful
            }.getOrDefault(false)
            if (isValid) {
                Log.d("FcmRepositoryImpl", "FCM 토큰 유효함, 갱신 불필요")
                return
            }
            // 유효하지 않으면 Firebase로 새 토큰 발급
            val newToken = FirebaseMessaging.getInstance().token.await()
            saveFcmToken(newToken)
            Log.d("FcmRepositoryImpl", "새 FCM 토큰 발급 및 저장 완료: $newToken")

            // 서버에 갱신 요청
            val response = runCatching {
                fcmUpdateService.updateFcmToken(
                    header = "Bearer $jwtToken",
                    body = mapOf("fcmToken" to newToken),
                )
            }
            response.onSuccess {
                Log.d("FcmRepositoryImpl", "서버에 FCM 토큰 갱신 성공")
            }.onFailure {
                Log.e("FcmRepositoryImpl", "서버에 토큰 갱신 실패: ${it.message}")
            }
        } catch (e: Exception) {
            Log.e("FcmRepositoryImpl", "validateAndRefreshTokenIfNeeded 실패: ${e.message}")
        }
    }
}
