package com.konkuk.medicarecall.data.network

import com.konkuk.medicarecall.data.repository.DataStoreRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // 1. 전화번호 인증, 회원가입 API는 헤더에 토큰을 추가하지 않음
        val path = originalRequest.url.encodedPath
        if (path.contains("verifications") || path.contains("members") || path.contains("auth/refresh")) {
            return chain.proceed(originalRequest)
        }

        // 2. DataStore에서 AccessToken을 동기적으로 가져옴
        // Interceptor는 백그라운드 스레드에서 실행되므로 runBlocking을 사용해도 UI를 차단하지 않음
        val accessToken = runBlocking {
            dataStoreRepository.getAccessToken()
        }
        // 4. 헤더에 AccessToken 추가하여 새로운 요청 생성
        val requestBuilder = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")

        return chain.proceed(requestBuilder.build())
    }
}
