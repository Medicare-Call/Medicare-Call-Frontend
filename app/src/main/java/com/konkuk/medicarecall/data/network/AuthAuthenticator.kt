package com.konkuk.medicarecall.data.network

import android.util.Log
import com.konkuk.medicarecall.data.api.auth.RefreshService
import com.konkuk.medicarecall.data.repository.DataStoreRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val refreshService: dagger.Lazy<RefreshService>, // 순환 참조 방지
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 1. 401 에러가 아니면 null 반환 (다른 에러는 이 Authenticator가 처리하지 않음)
        if (response.code != 401) {
            return null
        }

        // 2. 동기화 블록으로 여러 요청이 동시에 401을 받아도 토큰 갱신은 한 번만 실행되도록 보장
        synchronized(this) {
            // 3. 현재 저장된 토큰들을 가져옴
            val accessToken = runBlocking { dataStoreRepository.getAccessToken() }
            val refreshToken = runBlocking { dataStoreRepository.getRefreshToken() }

            // 4. 이전 요청의 AccessToken이 현재 저장된 AccessToken과 다르다면,
            //    다른 스레드에서 이미 토큰 갱신에 성공한 경우이므로 새로운 토큰으로 재요청
            val oldAccessToken = response.request.header("Authorization")?.removePrefix("Bearer ")
            if (oldAccessToken != null && oldAccessToken != accessToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
            }

            // 5. RefreshToken이 없으면 갱신 불가, null 반환
            if (refreshToken.isNullOrEmpty()) {
                // 여기서 로그인 화면으로 보내는 로직을 추가할 수 있습니다. (예: EventBus, SharedFlow 등)
                return null
            }

            // 6. 토큰 갱신 API 호출 (runBlocking 사용)
            val refreshResponse = runBlocking {
                refreshService.get().refreshToken(refreshToken)
            }

            return if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
                // 7. 토큰 갱신 성공 시, 새로운 토큰들을 DataStore에 저장
                val newTokens = refreshResponse.body()!!
                runBlocking {
                    dataStoreRepository.saveAccessToken(newTokens.accessToken)
                    dataStoreRepository.saveRefreshToken(newTokens.refreshToken)
                }
                Log.d("AuthAuthenticator", "Token refreshed successfully.")

                // 8. 기존 요청에 새로운 AccessToken을 담아 재요청
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.accessToken}")
                    .build()
            } else {
                // 9. 토큰 갱신 실패 시 (RefreshToken 만료 등), 저장된 토큰 삭제 후 null 반환
                Log.e(
                    "AuthAuthenticator",
                    "Failed to refresh token. Error code: ${refreshResponse.code()}",
                )
                runBlocking { dataStoreRepository.saveRefreshToken("") }
                // 여기서도 로그인 화면으로 보내는 로직 추가 가능
                null
            }
        }
    }
}
